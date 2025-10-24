package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.entities.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @DisplayName(
            """
            Given the authors table is empty
            When the findAll method is called
            Then an empty author list is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindAllIsCalled_thenReturnsEmptyList() {
        var authors = authorRepository.findAll();

        then(authors)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the authors table is populated by one author
            When the findAll method is called
            Then an author list is returned
            And it contains this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void givenTableIsPopulatedByOneAuthor_whenFindAllIsCalled_thenReturnsAListContainingThisAuthor() {
        var authors = authorRepository.findAll();

        then(authors)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(Author::getId)
                        .isEqualTo(Fixtures.AUTHOR_UUID);
    }

    @DisplayName(
            """
            Given the authors table is empty
            When the findById method is called with a random UUID
            Then an empty author optional is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindByIdIsCalledWithRandomUUID_thenReturnsEmptyOptional() {
        var authorOptional = authorRepository.findById(Fixtures.getRandomUUID());

        then(authorOptional)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the authors table is populated by one author
            When the findById method is called with the author's UUID
            Then a not empty author optional is returned
            And it contains this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void givenTableIsPopulatedByOneAuthor_whenFindByIdIsCalledWithAuthorUUID_thenReturnsAuthor() {
        var authorOptional = authorRepository.findById(Fixtures.AUTHOR_UUID);

        then(authorOptional)
                .isNotNull()
                .isNotEmpty()
                .get()
                    .extracting(Author::getId)
                        .isEqualTo(Fixtures.AUTHOR_UUID);
    }

    @DisplayName(
            """
            Given there is a transient author
            When the save method is called
            Then the persistence is success
            And the persisted author is return
            """)
    @Test
    void givenTransientAuthor_whenSaveIsCalled_thenPersistenceIsSuccess() {
        var transientAuthor = Fixtures.getTransientAuthor();
        given(transientAuthor)
                .extracting(Author::getId)
                    .isNull();

        var persistedAuthor = authorRepository.save(transientAuthor);

        then(transientAuthor)
                .isEqualTo(persistedAuthor)
                .extracting(Author::getId)
                    .isNotNull();
    }
}
