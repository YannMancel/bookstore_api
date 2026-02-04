package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.configuration.ApplicationConfiguration;
import com.mancel.yann.bookstore_api.data.repositories.AuthorPersistRepository;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import net.bytebuddy.utility.RandomString;
import org.h2.jdbc.JdbcSQLDataException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.hibernate.JDBCException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@Import(ApplicationConfiguration.class)
class AuthorPersistRepositoryTest {

    @Autowired
    @Qualifier("authorPersistRepositoryImpl")
    AuthorPersistRepository authorPersistRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    static Stream<Arguments> transientEntityWithNullableFieldGenerator() {
        return Stream.of(
                arguments(
                        new AuthorEntity(null, "John", "Doe"),
                        "EMAIL"),
                arguments(
                        new AuthorEntity("john.doe@gmail.com", null, "Doe"),
                        "FIRST_NAME"),
                arguments(
                        new AuthorEntity("john.doe@gmail.com", "John", null),
                        "LAST_NAME"));
    }

    static Stream<Arguments> transientEntityWithMoreThanMaxLengthGenerator() {
        return Stream.of(
                arguments(
                        new AuthorEntity(
                                RandomString.make(AuthorEntity.EMAIL_LENGTH + 1),
                                "John",
                                "Doe"),
                        MessageFormat.format(
                                "EMAIL CHARACTER VARYING({0})",
                                AuthorEntity.EMAIL_LENGTH)),
                arguments(
                        new AuthorEntity(
                                "john.doe@gmail.com",
                                RandomString.make(AuthorEntity.FIRST_NAME_LENGTH + 1),
                                "Doe"),
                        MessageFormat.format(
                                "FIRST_NAME CHARACTER VARYING({0})",
                                AuthorEntity.FIRST_NAME_LENGTH)),
                arguments(
                        new AuthorEntity(
                                "john.doe@gmail.com",
                                "John",
                                RandomString.make(AuthorEntity.LAST_NAME_LENGTH + 1)),
                        MessageFormat.format(
                                "LAST_NAME CHARACTER VARYING({0})",
                                AuthorEntity.LAST_NAME_LENGTH)));
    }

    @DisplayName("""
            Given there is a valid transient author
            When the save method is called
            Then the persistence is success
            And the author is returned
            """)
    @Test
    void test1() {
        var request = Fixtures.Author.getValidCreationRequest();
        var transientEntity = Fixtures.Author.MAPPER.toTransientEntity(request);

        var persistedAuthor = authorPersistRepository.save(transientEntity);

        then(persistedAuthor)
                .isNotNull()
                .extracting(AuthorEntity::id)
                .isNotNull();
    }

    @DisplayName("""
            Given there is an invalid transient author
            When the save method is called
            Then the transaction is fail
            And a ConstraintViolationException is thrown
            """)
    @ParameterizedTest
    @MethodSource("transientEntityWithNullableFieldGenerator")
    void test2(AuthorEntity transientEntity, String label) {
        authorPersistRepository.save(transientEntity);
        var thrown = catchThrowable(() -> testEntityManager.flush());

        then(thrown)
                .isExactlyInstanceOf(ConstraintViolationException.class)
                .isInstanceOf(JDBCException.class)
                .hasMessageStartingWith("could not execute statement")
                .hasMessageContaining(label)
                .extracting(Throwable::getCause)
                .isExactlyInstanceOf(JdbcSQLIntegrityConstraintViolationException.class)
                .isInstanceOf(SQLException.class)
                .isInstanceOf(Exception.class);
    }

    @DisplayName("""
            Given there is an invalid transient author
            When the save method is called
            Then the transaction is fail
            And a DataException is thrown
            """)
    @ParameterizedTest
    @MethodSource("transientEntityWithMoreThanMaxLengthGenerator")
    void test3(AuthorEntity transientEntity, String label) {
        authorPersistRepository.save(transientEntity);
        var thrown = catchThrowable(() -> testEntityManager.flush());

        then(thrown)
                .isExactlyInstanceOf(DataException.class)
                .isInstanceOf(JDBCException.class)
                .hasMessageStartingWith("could not execute statement")
                .hasMessageContaining(label)
                .extracting(Throwable::getCause)
                .isExactlyInstanceOf(JdbcSQLDataException.class)
                .isInstanceOf(SQLDataException.class)
                .isInstanceOf(Exception.class);
    }
}
