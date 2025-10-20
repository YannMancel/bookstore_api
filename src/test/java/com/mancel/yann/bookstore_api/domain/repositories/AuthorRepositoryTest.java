package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.entities.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

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
        var authorOptional = authorRepository.findById(Fixtures.getRandomUUID());

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
        var authorOptional = authorRepository.findById(Fixtures.AUTHOR_UUID);

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
        var transientAuthor = Fixtures.getTransientAuthor();
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
