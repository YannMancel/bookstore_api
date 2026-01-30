package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.data.repositories.AuthorPersistRepository;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import net.bytebuddy.utility.RandomString;
import org.h2.jdbc.JdbcSQLDataException;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.PropertyValueException;
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

import java.sql.SQLDataException;
import java.text.MessageFormat;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
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
                        "email"),
                arguments(
                        new AuthorEntity("john.doe@gmail.com", null, "Doe"),
                        "firstName"),
                arguments(
                        new AuthorEntity("john.doe@gmail.com", "John", null),
                        "lastName"));
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
            Then the persistence is fail
            And a PropertyValueException is thrown
            """)
    @ParameterizedTest
    @MethodSource("transientEntityWithNullableFieldGenerator")
    void test2(AuthorEntity transientEntity, String propertyName) {
        var thrown = catchThrowable(() -> authorPersistRepository.save(transientEntity));

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
                .hasMessageStartingWith(
                        MessageFormat.format(
                        "could not execute statement [Valeur trop longue pour la colonne \"{0}\"", label))
                .extracting(Throwable::getCause)
                .isExactlyInstanceOf(JdbcSQLDataException.class)
                .isInstanceOf(SQLDataException.class)
                .isInstanceOf(Exception.class);
    }
}
