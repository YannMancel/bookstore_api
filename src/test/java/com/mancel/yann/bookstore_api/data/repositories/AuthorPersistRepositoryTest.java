package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.entities.Author;
import org.assertj.core.api.Condition;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
            When the saveFromRequest method is called
            Then the persistence is success
            And the persisted author is returned
            """)
    @Test
    void test1() {
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

    static Stream<Arguments> invalidAuthorCreationRequestGenerator() {
        return Stream.of(
                arguments(
                        Fixtures.getInvalidAuthorCreationRequest(true, false),
                        "^not-null property references a null or transient value.*Author.firstName$"),
                arguments(Fixtures.getInvalidAuthorCreationRequest(false, true),
                        "^not-null property references a null or transient value.*Author.lastName$"),
                arguments(Fixtures.getInvalidAuthorCreationRequest(true, true),
                        "^not-null property references a null or transient value.*Author.firstName$")
        );
    }

    @DisplayName(
            """
            Given there is a invalid author creation request
            When the saveFromRequest method is called
            Then the persistence is fail
            And a PropertyValueException is thrown
            """)
    @ParameterizedTest
    @MethodSource("invalidAuthorCreationRequestGenerator")
    void test2(
            AuthorCreationRequest  authorCreationRequest,
            String regex
    ) {
        var thrown = catchThrowable(() -> authorPersistRepository.saveFromRequest(authorCreationRequest));

        then(thrown)
                .isExactlyInstanceOf(PropertyValueException.class)
                .isInstanceOf(RuntimeException.class)
                .hasMessageMatching(regex);
    }
}
