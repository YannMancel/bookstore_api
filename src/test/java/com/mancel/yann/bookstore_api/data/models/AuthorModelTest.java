package com.mancel.yann.bookstore_api.data.models;

import com.mancel.yann.bookstore_api.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

@DataJpaTest
class AuthorModelTest {

    @Autowired
    TestEntityManager entityManager;

    @DisplayName("""
            Given the table is empty
            When a JPQL query is called to find all authors
            Then an empty list is returned
            """)
    @Test
    void test1() {
        var authors = entityManager
                .getEntityManager()
                .createQuery("select m from AuthorModel m", AuthorModel.class)
                .getResultList();

        then(authors)
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
        var authors = entityManager
                .getEntityManager()
                .createQuery("select m from AuthorModel m", AuthorModel.class)
                .getResultList();

        then(authors)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                .extracting(AuthorModel::getId)
                .isEqualTo(Fixtures.Author.AUTHOR_UUID);
    }

    @DisplayName("""
            Given the table is empty
            When the find method is called with a random id
            Then null is returned
            """)
    @Test
    void test3() {
        var author = entityManager.find(AuthorModel.class, Fixtures.getRandomUUID());

        then(author).isNull();
    }

    @DisplayName("""
            Given the table is populated by one author
            When the find method is called with the author's id
            Then this author is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test4() {
        var author = entityManager.find(AuthorModel.class, Fixtures.Author.AUTHOR_UUID);

        then(author)
                .isNotNull()
                .extracting(AuthorModel::getId)
                .isEqualTo(Fixtures.Author.AUTHOR_UUID);
    }

    @DisplayName("""
            Given there is a transient author
            When the persist method is called
            Then the persistence is success
            And the persisted author is return
            """)
    @Test
    void test5() {
        var transientAuthor = Fixtures.Author.getTransientAuthorModel();
        given(transientAuthor)
                .extracting(AuthorModel::getId)
                .isNull();

        var persistedAuthor = entityManager.persist(transientAuthor);

        then(transientAuthor)
                .isEqualTo(persistedAuthor)
                .isEqualTo(entityManager.find(AuthorModel.class, persistedAuthor.getId()))
                .extracting(AuthorModel::getId)
                .isNotNull();
    }
}
