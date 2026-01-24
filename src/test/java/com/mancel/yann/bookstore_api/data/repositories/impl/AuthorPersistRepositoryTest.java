package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.data.repositories.AuthorPersistRepository;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
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

//    static Stream<Arguments> transientEntityWithMoreThanMaxLengthGenerator() {
//        return Stream.of(
//                arguments(
//                        new AuthorEntity(
//                                RandomString.make(AuthorEntity.EMAIL_LENGTH + 1),
//                                "John",
//                                "Doe"),
//                        "???"));
//    }

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

//    @DisplayName("""
//            Given there is an invalid transient author
//            When the save method is called
//            Then the persistence is fail
//            And a PropertyValueException is thrown
//            """)
//    @ParameterizedTest
//    @MethodSource("transientEntityWithMoreThanMaxLengthGenerator")
//    void test3(AuthorEntity transientEntity, String propertyName) {
//        var thrown = catchThrowable(() -> authorPersistRepository.save(transientEntity));
//
//        then(thrown)
//                .isExactlyInstanceOf(UnknownException.class)
//                .isInstanceOf(DomainException.class)
//                .hasMessageStartingWith("not-null property references a null or transient value")
//                .hasMessageEndingWith("AuthorModel." + propertyName)
//                .extracting(Throwable::getCause)
//                .isExactlyInstanceOf(PropertyValueException.class)
//                .isInstanceOf(HibernateException.class)
//                .isInstanceOf(RuntimeException.class);
//    }
}
