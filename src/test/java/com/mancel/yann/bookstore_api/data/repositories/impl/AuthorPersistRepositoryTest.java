package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.TestContainerInjector;
import com.mancel.yann.bookstore_api.configuration.DataConfiguration;
import com.mancel.yann.bookstore_api.data.repositories.AuthorPersistRepository;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import net.bytebuddy.utility.RandomString;
import org.hibernate.JDBCException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@ContextConfiguration(classes = DataConfiguration.class)
class AuthorPersistRepositoryTest extends TestContainerInjector {

    @Autowired
    @Qualifier("authorPersistRepositoryImpl")
    AuthorPersistRepository authorPersistRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    static Stream<Arguments> transientAuthorModelWithNullableFieldGenerator() {
        return Stream.of(
                arguments(
                        new AuthorModel(null, "John", "Doe"),
                        "email"),
                arguments(
                        new AuthorModel("john.doe@gmail.com", null, "Doe"),
                        "first_name"),
                arguments(
                        new AuthorModel("john.doe@gmail.com", "John", null),
                        "last_name"));
    }

    static Stream<Arguments> transientAuthorModelWithMoreThanMaxLengthGenerator() {
        return Stream.of(
                arguments(
                        new AuthorModel(
                                RandomString.make(AuthorModel.EMAIL_LENGTH + 1),
                                "John",
                                "Doe"),
                        AuthorModel.EMAIL_LENGTH),
                arguments(
                        new AuthorModel(
                                "john.doe@gmail.com",
                                RandomString.make(AuthorModel.FIRST_NAME_LENGTH + 1),
                                "Doe"),
                        AuthorModel.FIRST_NAME_LENGTH),
                arguments(
                        new AuthorModel(
                                "john.doe@gmail.com",
                                "John",
                                RandomString.make(AuthorModel.LAST_NAME_LENGTH + 1)),
                        AuthorModel.LAST_NAME_LENGTH));
    }

    @DisplayName("""
            Given there is a valid transient author
            When the save method is called
            Then the persistence is success
            And the author is returned
            """)
    @Test
    void persistTransientAuthorModel() {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();
        var transientAuthorModel = Fixtures.Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);

        var persistedAuthorModel = authorPersistRepository.save(transientAuthorModel);

        then(persistedAuthorModel)
                .isNotNull()
                .extracting(AuthorModel::id)
                    .isNotNull();
    }

    @DisplayName("""
            Given there is an invalid transient author
            When the save method is called
            Then the transaction is fail
            And a PSQLException is thrown
            """)
    @ParameterizedTest
    @MethodSource("transientAuthorModelWithNullableFieldGenerator")
    void shouldThrowAnExceptionForANullableField(
            AuthorModel transientAuthorModel,
            String label
    ) {
        var thrown = catchThrowable(() -> {
            authorPersistRepository.save(transientAuthorModel);
            testEntityManager.flush();
        });

        then(thrown)
                .isExactlyInstanceOf(ConstraintViolationException.class)
                .isInstanceOf(JDBCException.class)
                .hasMessageStartingWith("could not execute statement")
                .hasMessageContaining(label)
                .extracting(Throwable::getCause)
                    .isExactlyInstanceOf(PSQLException.class)
                    .isInstanceOf(SQLException.class)
                    .isInstanceOf(Exception.class);
    }

    @DisplayName("""
            Given there is an invalid transient author
            When the save method is called
            Then the transaction is fail
            And a PSQLException is thrown
            """)
    @ParameterizedTest
    @MethodSource("transientAuthorModelWithMoreThanMaxLengthGenerator")
    void shouldThrowAnExceptionForAFieldLengthConstraint(
            AuthorModel transientAuthorModel,
            int length
    ) {
        var thrown = catchThrowable(() -> {
            authorPersistRepository.save(transientAuthorModel);
            testEntityManager.flush();
        });

        then(thrown)
                .isExactlyInstanceOf(DataException.class)
                .isInstanceOf(JDBCException.class)
                .hasMessageStartingWith("could not execute statement")
                .hasMessageContaining(
                        MessageFormat.format(
                                "value too long for type character varying({0})",
                                length))
                .extracting(Throwable::getCause)
                    .isExactlyInstanceOf(PSQLException.class)
                    .isInstanceOf(SQLException.class)
                    .isInstanceOf(Exception.class);
    }
}
