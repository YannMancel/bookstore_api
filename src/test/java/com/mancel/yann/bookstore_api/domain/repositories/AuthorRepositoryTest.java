package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.entities.Author;
import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @DisplayName(
            """
            Given table is empty
            When findAll method is called
            Then empty list is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindAllIsCalled_thenReturnsEmptyList() {
        var authors = authorRepository.findAll();

        assertThat(authors)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is populated by one author
            When findAll method is called
            Then a list containing this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void givenTableIsPopulatedByOneAuthor_whenFindAllIsCalled_thenReturnsAListContainingThisAuthor() {
        var authors = authorRepository.findAll();

        assertThat(authors)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @DisplayName(
            """
            Given table is empty
            When findById method is called with a random UUID
            Then empty optional is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindByIdIsCalledWithRandomUUID_thenReturnsEmptyOptional() {
        var uuid = UUID.randomUUID();
        var authorOptional = authorRepository.findById(uuid);

        assertThat(authorOptional)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is populated by one author
            When findById method is called with the author's UUID
            Then this author is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void givenTableIsPopulatedByOneAuthor_whenFindByIdIsCalledWithAuthorUUID_thenReturnsAuthor() {
        var uuid = UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");
        var authorOptional = authorRepository.findById(uuid);

        assertThat(authorOptional)
                .isNotNull()
                .isNotEmpty();
    }

    @DisplayName(
            """
            Given a transient author
            When save method is called
            Then the persistence is success
            """)
    @Test
    void givenTransientAuthor_whenSaveIsCalled_thenPersistenceIsSuccess() {
        var transientAuthor = new Author(
                "john.doe@gmail.com",
                "John",
                "Doe");
        assertThat(transientAuthor)
                .extracting(Author::getId)
                .isNull();

        var persistedAuthor = authorRepository.save(transientAuthor);

        assertThat(transientAuthor)
                .isEqualTo(persistedAuthor)
                .extracting(Author::getId)
                    .isNotNull();
    }
}
