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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

@DataJpaTest
@ContextConfiguration(classes = DataConfiguration.class)
class AuthorModelTest extends TestContainerInjector {

    @Autowired
    TestEntityManager entityManager;

    @DisplayName("""
            Given the table is empty
            When a JPQL query is called to find all authors
            Then an empty list is returned
            """)
    @Test
    void test1() {
        var persistedModels = entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM AuthorModel m", AuthorModel.class)
                .getResultList();

        then(persistedModels)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the table is populated by one author
            When a JPQL query is called to find all authors
            Then an list is returned with this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test2() {
        var persistedModels = entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM AuthorModel m", AuthorModel.class)
                .getResultList();

        then(persistedModels)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(AuthorModel::getId)
                        .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given the table is empty
            When the find method is called with a random id
            Then null is returned
            """)
    @Test
    void test3() {
        var persistedModel = entityManager.find(AuthorModel.class, Fixtures.getRandomUUID());

        then(persistedModel).isNull();
    }

    @DisplayName("""
            Given the table is populated by one author
            When the find method is called with the author's id
            Then this author is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test4() {
        var persistedModel = entityManager.find(AuthorModel.class, Fixtures.Author.UUID);

        then(persistedModel)
                .isNotNull()
                .extracting(AuthorModel::getId)
                    .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given there is a transient author
            When the persist method is called
            Then the persistence is success
            And the persisted author is return
            """)
    @Test
    void test5() {
        var transientModel = Fixtures.Author.getTransientModel();
        given(transientModel)
                .extracting(AuthorModel::getId)
                    .isNull();

        var persistedModel = entityManager.persist(transientModel);

        then(transientModel)
                .isEqualTo(persistedModel)
                .isEqualTo(entityManager.find(AuthorModel.class, persistedModel.getId()))
                .extracting(AuthorModel::getId)
                    .isNotNull();
    }
}
