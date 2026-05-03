package com.mancel.yann.bookstore_api.data.models;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.TestContainerInjector;
import com.mancel.yann.bookstore_api.configuration.DataConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.text.MessageFormat;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

@DataJpaTest
@ContextConfiguration(classes = DataConfiguration.class)
class BookModelTest extends TestContainerInjector {

    @Autowired
    TestEntityManager entityManager;

    @DisplayName("""
            Given the books table is empty
            When a JPQL query is called to find all entities
            Then an empty book list is returned
            """)
    @Test
    void test1() {
        var persistedModels = entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM BookModel m", BookModel.class)
                .getResultList();

        then(persistedModels)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test2() {
        var persistedModels = entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM BookModel m", BookModel.class)
                .getResultList();

        then(persistedModels)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(BookModel::getId)
                        .isEqualTo(Fixtures.Book.UUID);
    }

    @DisplayName("""
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on author's UUID of this book
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test3() {
        var persistedModels = entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM BookModel m WHERE m.author.id=:authorId", BookModel.class)
                .setParameter("authorId", Fixtures.Author.UUID)
                .getResultList();

        then(persistedModels)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(BookModel::getId)
                        .isEqualTo(Fixtures.Book.UUID);
    }

    @DisplayName("""
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on author's UUID with a random value
            Then an empty book list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test4() {
        var persistedModels = entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM BookModel m WHERE m.author.id=:authorId", BookModel.class)
                .setParameter("authorId", Fixtures.getRandomUUID())
                .getResultList();

        then(persistedModels)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on book's title with a subtitle of book's title
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test5() {
        var pattern = MessageFormat.format("%{0}%", Fixtures.Book.TITLE);
        var persistedModels = entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM BookModel m WHERE m.title LIKE :title", BookModel.class)
                .setParameter("title", pattern)
                .getResultList();

        then(persistedModels)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(BookModel::getId)
                        .isEqualTo(Fixtures.Book.UUID);
    }

    @DisplayName("""
            Given the books table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on book's title with a random subtitle
            Then an empty book list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test6() {
        var pattern = MessageFormat.format("%{0}%", Fixtures.getRandomUUID());
        var persistedModels = entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM BookModel m WHERE m.title LIKE :title", BookModel.class)
                .setParameter("title", pattern)
                .getResultList();

        then(persistedModels)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the books table is empty
            When the find method is called with a random UUID
            Then null is returned
            """)
    @Test
    void test7() {
        var persistedModel = entityManager.find(BookModel.class, Fixtures.getRandomUUID());

        then(persistedModel).isNull();
    }

    @DisplayName("""
            Given the books table is populated by one book
            When the find method is called with the book's UUID
            Then this book is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test8() {
        var persistedModel = entityManager.find(BookModel.class, Fixtures.Book.UUID);

        then(persistedModel)
                .isNotNull()
                .extracting(BookModel::getId)
                    .isEqualTo(Fixtures.Book.UUID);
    }

    @DisplayName("""
            Given there is a persisted author
            And there is a transient book
            When the persist method is called
            Then the persistence is success
            And the persisted book is return
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test9() {
        var persistedAuthorModel = entityManager.find(AuthorModel.class, Fixtures.Author.UUID);
        var transientBookModel = Fixtures.Book.getTransientModel();
        given(persistedAuthorModel)
                .isNotNull()
                .extracting(AuthorModel::getId)
                    .isEqualTo(transientBookModel.getAuthor().getId());
        given(transientBookModel)
                .extracting(BookModel::getId)
                    .isNull();

        var persistedBookModel = entityManager.persist(transientBookModel);

        then(transientBookModel)
                .isEqualTo(persistedBookModel)
                .isEqualTo(entityManager.find(BookModel.class, persistedBookModel.getId()))
                .extracting(BookModel::getId)
                    .isNotNull();
    }
}
