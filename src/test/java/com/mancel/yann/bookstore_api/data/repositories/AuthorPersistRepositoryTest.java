package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.entities.Author;
import org.assertj.core.api.Condition;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class AuthorPersistRepositoryTest {

    @Autowired
    @Qualifier("authorPersistRepositoryImpl")
    AuthorPersistRepository authorPersistRepository;

    AuthorCreationRequest convertAuthorToAuthorCreationRequest(Author author) {
        return new AuthorCreationRequest(
                author.getEmail(),
                author.getFirstName(),
                author.getLastName());
    }

    @DisplayName(
            """
            Given there is a valid author creation request
            When saveFromRequest method is called
            Then the persistence is success
            And the persisted author is returned
            """)
    @Test
    void givenAValidAuthorCreationRequest_whenSaveFromRequestIsCalled_thenPersistenceIsSuccess() {
        var authorCreationRequest = Fixtures.getValidAuthorCreationRequest();

        var persistedAuthor = authorPersistRepository.saveFromRequest(authorCreationRequest);

        then(persistedAuthor)
                .isNotNull()
                .is(new Condition<>(
                        author -> convertAuthorToAuthorCreationRequest(author).equals(authorCreationRequest),
                        "is equal to the author creation request"))
                .extracting(Author::getId)
                    .isNotNull();
    }

    @DisplayName(
            """
            Given there is a invalid author creation request
            When saveFromRequest method is called
            Then the persistence is fail
            And a PropertyValueException is thrown
            """)
    @Test
    void givenAInvalidAuthorCreationRequest_whenSaveFromRequestIsCalled_thenPersistenceIsFail() {
        var authorCreationRequest = Fixtures.getInvalidAuthorCreationRequest(true, false);

        var thrown = catchThrowable(() -> authorPersistRepository.saveFromRequest(authorCreationRequest));

        then(thrown)
                .isExactlyInstanceOf(PropertyValueException.class)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContainingAll(
                        "not-null property references a null or transient value",
                        "Author.firstName");
    }
}
