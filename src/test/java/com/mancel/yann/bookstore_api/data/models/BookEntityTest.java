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
class BookEntityTest extends TestContainerInjector {

    @Autowired
    TestEntityManager entityManager;

    @DisplayName("""
            Given the books table is populated by 2 books
            When a JPQL query is called to find all entities
            Then the book list is returned
            And it contains 2 books
            """)
    @Test
    @Sql({"/scripts/insert_two_books_with_at_least_one_author_in_common.sql"})
    void findAllPersistedBookEntities() {
        var persistedBookEntities = entityManager
                .getEntityManager()
                .createQuery(
                        "SELECT b FROM BookEntity b " +
                                "JOIN FETCH b.authors",
                        BookEntity.class)
                .getResultList();

        then(persistedBookEntities)
                .isNotNull()
                .hasSize(2)
                .allMatch(bookEntity -> bookEntity.getAuthors()
                        .stream()
                        .anyMatch(authorModel ->  authorModel.getId()
                                .equals(Fixtures.Author.UUID_FOR_MULTIPLE_BOOKS)));
    }

    @DisplayName("""
            Given the books table is populated by 2 books
            When a JPQL query is called to find all entities
            And there is a filter on author's UUID
            Then a book list is returned
            And it contains 1 book
            """)
    @Test
    @Sql({"/scripts/insert_two_books_with_at_least_one_author_in_common.sql"})
    void findAllPersistedBookEntitiesForAnAuthor() {
        var persistedBookEntities = entityManager
                .getEntityManager()
                .createQuery(
                        "SELECT b FROM BookEntity b " +
                                "JOIN FETCH b.authors a " +
                                "WHERE a.id = :authorId",
                        BookEntity.class)
                .setParameter("authorId", Fixtures.Author.UUID_FOR_SINGLE_BOOK)
                .getResultList();

        then(persistedBookEntities)
                .isNotNull()
                .hasSize(1)
                .allMatch(bookEntity -> bookEntity.getAuthors()
                        .stream()
                        .anyMatch(authorModel ->  authorModel.getId()
                                .equals(Fixtures.Author.UUID_FOR_SINGLE_BOOK)));
    }

    @DisplayName("""
            Given the books table is populated by 2 books
            When a JPQL query is called to find all entities
            And there is a filter on book's title with a subtitle of book's title
            Then a book list is returned
            And it contains 1 book
            """)
    @Test
    @Sql({"/scripts/insert_two_books_with_at_least_one_author_in_common.sql"})
    void findAllPersistedBookEntitiesByTitlePattern() {
        var persistedBookEntities = entityManager
                .getEntityManager()
                .createQuery(
                        "SELECT b FROM BookEntity b " +
                                "JOIN FETCH b.authors " +
                                "WHERE b.title LIKE :pattern",
                        BookEntity.class)
                .setParameter("pattern", MessageFormat.format("%{0}%", Fixtures.Book.TITLE))
                .getResultList();

        then(persistedBookEntities)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .allMatch(bookEntity -> bookEntity.getId()
                        .equals(Fixtures.Book.UUID));
    }

    @DisplayName("""
            Given the books table is populated by 2 books
            When the find method is called with the book's UUID
            Then this book is returned
            """)
    @Test
    @Sql({"/scripts/insert_two_books_with_at_least_one_author_in_common.sql"})
    void findPersistedBookEntityByItsId() {
        var persistedBookEntity = entityManager.find(
                BookEntity.class,
                Fixtures.Book.UUID);

        then(persistedBookEntity)
                .isNotNull()
                .extracting(BookEntity::getId)
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
    @Sql({"/scripts/insert_two_books_with_at_least_one_author_in_common.sql"})
    void persistTransientBookEntity() {
        var transientBookEntity = Fixtures.Book.getTransientEntity();
        given(transientBookEntity)
                .extracting(BookEntity::getId)
                    .isNull();

        var persistedBookEntity = entityManager.persist(transientBookEntity);
        entityManager.flush();

        then(persistedBookEntity)
                .isEqualTo(transientBookEntity)
                .extracting(BookEntity::getId)
                    .isNotNull();
    }
}
