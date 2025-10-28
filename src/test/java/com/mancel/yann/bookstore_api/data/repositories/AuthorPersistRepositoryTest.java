package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import org.assertj.core.api.Condition;
import org.hibernate.HibernateException;
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

    AuthorCreationRequest convertEntityToRequest(AuthorEntity entity) {
        return new AuthorCreationRequest(
                entity.email(),
                entity.firstName(),
                entity.lastName());
    }

    @DisplayName(
            """
            Given there is a valid request
            When the saveFromRequest method is called
            Then the persistence is success
            And the entity is returned
            """)
    @Test
    void test1() {
        var request = Fixtures.Author.getValidAuthorCreationRequest();

        var persistedAuthor = authorPersistRepository.saveFromRequest(request);

        then(persistedAuthor)
                .isNotNull()
                .is(new Condition<>(
                        author -> convertEntityToRequest(author).equals(request),
                        "is equal to the request"))
                .extracting(AuthorEntity::id)
                    .isNotNull();
    }

    static Stream<Arguments> invalidRequestGenerator() {
        return Stream.of(
                arguments(
                        Fixtures.Author.getInvalidAuthorCreationRequest(true, false),
                        "firstName"),
                arguments(Fixtures.Author.getInvalidAuthorCreationRequest(false, true),
                        "lastName"),
                arguments(Fixtures.Author.getInvalidAuthorCreationRequest(true, true),
                        "firstName")
        );
    }

    @DisplayName(
            """
            Given there is a invalid request
            When the saveFromRequest method is called
            Then the persistence is fail
            And a PropertyValueException is thrown
            """)
    @ParameterizedTest
    @MethodSource("invalidRequestGenerator")
    void test2(AuthorCreationRequest request, String propertyName) {
        var thrown = catchThrowable(() -> authorPersistRepository.saveFromRequest(request));

        then(thrown)
                .isExactlyInstanceOf(UnknownException.class)
                .isInstanceOf(DomainException.class)
                .hasMessageStartingWith("not-null property references a null or transient value")
                .hasMessageEndingWith("AuthorModel." + propertyName)
                .extracting(Throwable::getCause)
                    .isExactlyInstanceOf(PropertyValueException.class)
                    .isInstanceOf(HibernateException.class)
                    .isInstanceOf(RuntimeException.class);
    }
}
