package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.data.repositories.BookPersistRepository;
import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.NoEntityFoundException;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;
import org.assertj.core.api.Condition;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.text.MessageFormat;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class BookPersistRepositoryTest {

    @Autowired
    @Qualifier("bookPersistRepositoryImpl")
    BookPersistRepository bookPersistRepository;

    BookCreationRequest convertEntityToRequest(BookEntity entity) {
        return new BookCreationRequest(
                entity.title(),
                entity.author().id());
    }

    @DisplayName("""
            Given there is a valid request
            When the saveFromRequest method is called
            Then the persistence is success
            And the entity is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test1() {
        var request = Fixtures.Book.getValidBookCreationRequest();

        var persistedBook = bookPersistRepository.saveFromRequest(request);

        then(persistedBook)
                .isNotNull()
                .is(new Condition<>(
                        entity -> convertEntityToRequest(entity).equals(request),
                        "is equal to the request"))
                .extracting(BookEntity::id)
                .isNotNull();
    }

    @DisplayName("""
            Given there is a invalid request with title equals to null
            When the saveFromRequest method is called
            Then the persistence is fail
            And a PropertyValueException is thrown
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test2() {
        var request = new BookCreationRequest(null, Fixtures.Author.AUTHOR_UUID);

        var thrown = catchThrowable(() -> bookPersistRepository.saveFromRequest(request));

        then(thrown)
                .isExactlyInstanceOf(UnknownException.class)
                .isInstanceOf(DomainException.class)
                .hasMessageStartingWith("not-null property references a null or transient value")
                .hasMessageEndingWith("BookModel.title")
                .extracting(Throwable::getCause)
                .isExactlyInstanceOf(PropertyValueException.class)
                .isInstanceOf(HibernateException.class)
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("""
            Given there is a invalid request with author's id equals to null
            When the saveFromRequest method is called
            Then the persistence is fail
            And an IllegalArgumentException is thrown
            """)
    @Test
    void test3() {
        var request = new BookCreationRequest("foo", null);

        var thrown = catchThrowable(() -> bookPersistRepository.saveFromRequest(request));

        then(thrown)
                .isExactlyInstanceOf(UnknownException.class)
                .isInstanceOf(DomainException.class)
                .hasMessage("id to load is required for loading")
                .extracting(Throwable::getCause)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("""
            Given there is a valid request with a random author's id
            When the saveFromRequest method is called
            Then the persistence is fail
            And a NoEntityFoundException is thrown
            """)
    @Test
    void test4() {
        var authorId = Fixtures.getRandomUUID();
        var request = new BookCreationRequest("foo", authorId);

        var thrown = catchThrowable(() -> bookPersistRepository.saveFromRequest(request));

        then(thrown)
                .isExactlyInstanceOf(NoEntityFoundException.class)
                .isInstanceOf(DomainException.class)
                .hasMessage(MessageFormat.format("Author is not found with {0}", authorId.toString()));
    }
}
