package com.mancel.yann.bookstore_api.data.entities;

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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

@DataJpaTest
@ContextConfiguration(classes = DataConfiguration.class)
class AuthorEntityTest extends TestContainerInjector {

    @Autowired
    TestEntityManager entityManager;

    @DisplayName("""
            Given the table is populated by 1 author
            When a JPQL query is called to find all authors
            Then an list is returned with 1 author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void findAllPersistedAuthorEntities() {
        var persistedAuthorEntities = entityManager
                .getEntityManager()
                .createQuery(
                        "SELECT a FROM AuthorEntity a",
                        AuthorEntity.class)
                .getResultList();

        then(persistedAuthorEntities)
                .isNotNull()
                .hasSize(1)
                .allMatch(authorEntity -> authorEntity.getId()
                        .equals(Fixtures.Author.UUID));
    }

    @DisplayName("""
            Given the table is populated by 1 author
            When the find method is called with the author's id
            Then this author is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void findPersistedAuthorEntityByItsId() {
        var persistedAuthorEntity = entityManager.find(
                AuthorEntity.class,
                Fixtures.Author.UUID);

        then(persistedAuthorEntity)
                .isNotNull()
                .extracting(AuthorEntity::getId)
                    .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given there is a transient author
            When the persist method is called
            Then the persistence is success
            And the persisted author is return
            """)
    @Test
    void persistTransientAuthorEntity() {
        var transientAuthorEntity = Fixtures.Author.getTransientEntity();
        given(transientAuthorEntity)
                .extracting(AuthorEntity::getId)
                    .isNull();

        var persistedAuthorEntity = entityManager.persist(transientAuthorEntity);
        entityManager.flush();

        then(persistedAuthorEntity)
                .isEqualTo(transientAuthorEntity)
                .extracting(AuthorEntity::getId)
                    .isNotNull();
    }
}
