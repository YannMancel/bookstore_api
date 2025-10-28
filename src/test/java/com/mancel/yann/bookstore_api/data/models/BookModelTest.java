package com.mancel.yann.bookstore_api.data.models;

import com.mancel.yann.bookstore_api.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.text.MessageFormat;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

@DataJpaTest
class BookModelTest {

    @Autowired
    TestEntityManager entityManager;

    @DisplayName(
            """
            Given the books table is empty
            When a JPQL query is called to find all entities
            Then an empty book list is returned
            """)
    @Test
    void test1() {
        var books = entityManager
                .getEntityManager()
                .createQuery("select m from BookModel m", BookModel.class)
                .getResultList();

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test2() {
        var books = entityManager
                .getEntityManager()
                .createQuery("select m from BookModel m", BookModel.class)
                .getResultList();

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(BookModel::getId)
                        .isEqualTo(Fixtures.Book.BOOK_UUID);
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on author's UUID of this book
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test3() {
        var books = entityManager
                .getEntityManager()
                .createQuery("select m from BookModel m where m.author.id=:authorId", BookModel.class)
                .setParameter("authorId", Fixtures.Author.AUTHOR_UUID)
                .getResultList();

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(BookModel::getId)
                        .isEqualTo(Fixtures.Book.BOOK_UUID);
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on author's UUID with a random value
            Then an empty book list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test4() {
        var books = entityManager
                .getEntityManager()
                .createQuery("select m from BookModel m where m.author.id=:authorId", BookModel.class)
                .setParameter("authorId", Fixtures.getRandomUUID())
                .getResultList();

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on book's title with a subtitle of book's title
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test5() {
        var randomSubtitle = MessageFormat.format("%{0}%", Fixtures.Book.BOOK_SUBTITLE);
        var books = entityManager
                .getEntityManager()
                .createQuery("select m from BookModel m where m.title like :title", BookModel.class)
                .setParameter("title", randomSubtitle)
                .getResultList();

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(BookModel::getId)
                        .isEqualTo(Fixtures.Book.BOOK_UUID);
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on book's title with a random subtitle
            Then an empty book list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test6() {
        var randomSubtitle = MessageFormat.format("%{0}%", Fixtures.getRandomUUID());
        var books = entityManager
                .getEntityManager()
                .createQuery("select m from BookModel m where m.title like :title", BookModel.class)
                .setParameter("title", randomSubtitle)
                .getResultList();

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the books table is empty
            When the find method is called with a random UUID
            Then null is returned
            """)
    @Test
    void test7() {
        var book = entityManager.find(BookModel.class, Fixtures.getRandomUUID());

        then(book).isNull();
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When the find method is called with the book's UUID
            Then this book is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test8() {
        var book = entityManager.find(BookModel.class, Fixtures.Book.BOOK_UUID);

        then(book)
                .isNotNull()
                .extracting(BookModel::getId)
                    .isEqualTo(Fixtures.Book.BOOK_UUID);
    }

    @DisplayName(
            """
            Given there is a persisted author
            And there is a transient book
            When the persist method is called
            Then the persistence is success
            And the persisted book is return
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test9() {
        var persistedAuthor = entityManager.find(AuthorModel.class, Fixtures.Author.AUTHOR_UUID);

        var transientBook = Fixtures.Book.getTransientBookModel(persistedAuthor.getAuthorEntity());
        given(transientBook)
                .extracting(BookModel::getId)
                    .isNull();

        var persistedBook = entityManager.persist(transientBook);

        then(transientBook)
                .isEqualTo(persistedBook)
                .isEqualTo(entityManager.find(BookModel.class, persistedBook.getId()))
                .extracting(BookModel::getId)
                    .isNotNull();
    }
}
