package com.mancel.yann.bookstore_api.entities;

import com.mancel.yann.bookstore_api.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorTest {

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
        var authors = entityManager
                .getEntityManager()
                .createQuery("select c from Author c", Author.class)
                .getResultList();

        assertThat(authors)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is populated by one author
            When a JPQL query is called to find all entities
            Then a list containing this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void givenTableIsPopulatedByOneAuthor_whenFindAllQueryIsCalled_thenReturnsAListContainingThisAuthor() {
        var authors = entityManager
                .getEntityManager()
                .createQuery("select c from Author c", Author.class)
                .getResultList();

        assertThat(authors)
                .isNotNull()
                .isNotEmpty().hasSize(1);
    }

    @DisplayName(
            """
            Given table is empty
            When find method is called with a random UUID
            Then null is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindIsCalledWithRandomUUID_thenReturnsNull() {
        var author = entityManager.find(Author.class, Fixtures.getRandomUUID());

        assertThat(author).isNull();
    }

    @DisplayName(
            """
            Given table is populated by one author
            When find method is called with the author's UUID
            Then this author is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void givenTableIsPopulatedByOneAuthor_whenFindIsCalledWithAuthorUUID_thenReturnsAuthor() {
        var author = entityManager.find(Author.class, Fixtures.AUTHOR_UUID);

        assertThat(author).isNotNull();
    }

    @DisplayName(
            """
            Given a transient author
            When persist method is called
            Then the persistence is success
            """)
    @Test
    void givenTransientAuthor_whenPersistIsCalled_thenPersistenceIsSuccess() {
        var transientAuthor = Fixtures.getTransientAuthor();
        assertThat(transientAuthor)
                .extracting(Author::getId)
                .isNull();

        var persistedAuthor = entityManager.persist(transientAuthor);

        assertThat(transientAuthor)
                .isEqualTo(persistedAuthor)
                .isEqualTo(entityManager.find(Author.class, persistedAuthor.getId()))
                .extracting(Author::getId)
                .isNotNull();
    }
}
