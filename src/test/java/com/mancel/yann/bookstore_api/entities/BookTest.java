package com.mancel.yann.bookstore_api.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.text.MessageFormat;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookTest {

    @Autowired
    TestEntityManager entityManager;

    @DisplayName(
            """
            Given table is empty
            When a JPQL query is called to find all entities
            Then empty list is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindAllQueryIsCalled_thenReturnsEmptyList() {
        var books = entityManager
                .getEntityManager()
                .createQuery("select c from Book c", Book.class)
                .getResultList();

        assertThat(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is populated by one book
            When a JPQL query is called to find all entities
            Then a list containing this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllQueryIsCalled_thenReturnsAListContainingThisBook() {
        var books = entityManager
                .getEntityManager()
                .createQuery("select c from Book c", Book.class)
                .getResultList();

        assertThat(books)
                .isNotNull()
                .isNotEmpty().hasSize(1);
    }

    @DisplayName(
            """
            Given table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on author's UUID of this book
            Then a list containing this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByAuthorIdQueryIsCalled_thenReturnsAListContainingThisBook() {
        var uuid = UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");
        var books = entityManager
                .getEntityManager()
                .createQuery("select b from Book b where b.author.id=:authorId", Book.class)
                .setParameter("authorId", uuid)
                .getResultList();

        assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @DisplayName(
            """
            Given table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on author's UUID with random value
            Then an empty list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByAuthorIdQueryIsCalled_thenReturnsAnEmptyListIsReturned() {
        var uuid = UUID.randomUUID();
        var books = entityManager
                .getEntityManager()
                .createQuery("select b from Book b where b.author.id=:authorId", Book.class)
                .setParameter("authorId", uuid)
                .getResultList();

        assertThat(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on book's title with a subtitle of book's title
            Then a list containing this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByTitleContainingQueryIsCalled_thenReturnsAListContainingThisBook() {
        var randomSubtitle = "%Horde%";
        var books = entityManager
                .getEntityManager()
                .createQuery("select b from Book b where b.title like :title", Book.class)
                .setParameter("title", randomSubtitle)
                .getResultList();

        assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @DisplayName(
            """
            Given table is populated by one book
            When a JPQL query is called to find all entities
            And there is a filter on book's title with a random subtitle
            Then an empty list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByTitleContainingQueryIsCalled_thenReturnsAnEmptyListIsReturned() {
        var randomSubtitle = MessageFormat.format("%{0}%", UUID.randomUUID());
        var books = entityManager
                .getEntityManager()
                .createQuery("select b from Book b where b.title like :title", Book.class)
                .setParameter("title", randomSubtitle)
                .getResultList();

        assertThat(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is empty
            When find method is called with a random UUID
            Then null is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindIsCalledWithRandomUUID_thenReturnsNull() {
        var uuid = UUID.randomUUID();
        var book = entityManager.find(Book.class, uuid);

        assertThat(book).isNull();
    }

    @DisplayName(
            """
            Given table is populated by one book
            When find method is called with the book's UUID
            Then this book is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindIsCalledWithBookUUID_thenReturnsBook() {
        var uuid = UUID.fromString("1955a2d7-5367-4c63-8323-31ad9bd3db31");
        var book = entityManager.find(Book.class, uuid);

        assertThat(book).isNotNull();
    }

    @DisplayName(
            """
            Given an author is persisted
            And a transient book
            When persist method is called
            Then the persistence is success
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTransientBook_whenPersistIsCalled_thenPersistenceIsSuccess() {
        var uuid = UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");
        var persistedAuthor = entityManager.find(Author.class, uuid);

        var transientBook = new Book(
                "Berserk",
                persistedAuthor);
        assertThat(transientBook)
                .extracting(Book::getId)
                .isNull();

        var persistedBook = entityManager.persist(transientBook);

        assertThat(transientBook)
                .isEqualTo(persistedBook)
                .isEqualTo(entityManager.find(Book.class, persistedBook.getId()))
                .extracting(Book::getId)
                .isNotNull();
    }
}