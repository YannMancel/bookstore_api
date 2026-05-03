package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.TestContainerInjector;
import com.mancel.yann.bookstore_api.configuration.DataConfiguration;
import com.mancel.yann.bookstore_api.data.repositories.AuthorPersistRepository;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
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

    static Stream<Arguments> transientEntityWithNullableFieldGenerator() {
        return Stream.of(
                arguments(
                        new AuthorEntity(null, "John", "Doe"),
                        "email"),
                arguments(
                        new AuthorEntity("john.doe@gmail.com", null, "Doe"),
                        "first_name"),
                arguments(
                        new AuthorEntity("john.doe@gmail.com", "John", null),
                        "last_name"));
    }

    static Stream<Arguments> transientEntityWithMoreThanMaxLengthGenerator() {
        return Stream.of(
                arguments(
                        new AuthorEntity(
                                RandomString.make(AuthorEntity.EMAIL_LENGTH + 1),
                                "John",
                                "Doe"),
                        AuthorEntity.EMAIL_LENGTH),
                arguments(
                        new AuthorEntity(
                                "john.doe@gmail.com",
                                RandomString.make(AuthorEntity.FIRST_NAME_LENGTH + 1),
                                "Doe"),
                        AuthorEntity.FIRST_NAME_LENGTH),
                arguments(
                        new AuthorEntity(
                                "john.doe@gmail.com",
                                "John",
                                RandomString.make(AuthorEntity.LAST_NAME_LENGTH + 1)),
                        AuthorEntity.LAST_NAME_LENGTH));
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
        var transientEntity = Fixtures.Author.CONTROLLER_MAPPER.toTransientEntity(request);

        var persistedEntity = authorPersistRepository.save(transientEntity);

        then(persistedEntity)
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
                    .isExactlyInstanceOf(PSQLException.class)
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
    void test3(AuthorEntity transientEntity, int length) {
        authorPersistRepository.save(transientEntity);
        var thrown = catchThrowable(() -> testEntityManager.flush());

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
