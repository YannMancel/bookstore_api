package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @DisplayName("""
            Given the table is empty
            When the findAll method is called
            Then an empty list is returned
            """)
    @Test
    void test1() {
        var books = bookRepository.findAll();

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the table is populated by one book
            When the findAll method is called
            Then a list is returned with this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test2() {
        var books = bookRepository.findAll();

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                .extracting(BookEntity::id)
                .isEqualTo(Fixtures.Book.BOOK_UUID);
    }

    @DisplayName("""
            Given the table is populated by one book
            When the findAllByAuthorId method is called with the book's id
            Then a list is returned with this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test3() {
        var books = bookRepository.findAllByAuthorId(Fixtures.Author.AUTHOR_UUID);

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                .extracting(BookEntity::id)
                .isEqualTo(Fixtures.Book.BOOK_UUID);
    }

    @DisplayName("""
            Given the table is populated by one book
            When the findAllByAuthorId method is called with a random id
            Then an empty list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test4() {
        var books = bookRepository.findAllByAuthorId(Fixtures.getRandomUUID());

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the table is populated by one book
            When the findAllByTitleContaining method is called with a subtitle of book's title
            Then a list is returned with this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test5() {
        var books = bookRepository.findAllByTitleContaining(Fixtures.Book.BOOK_SUBTITLE);

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                .extracting(BookEntity::id)
                .isEqualTo(Fixtures.Book.BOOK_UUID);
    }

    @DisplayName("""
            Given the table is populated by one book
            When the findAllByTitleContaining method is called with a random subtitle
            Then an empty list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test6() {
        var randomSubtitle = Fixtures.getRandomUUID().toString();

        var books = bookRepository.findAllByTitleContaining(randomSubtitle);

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the table is empty
            When the findById method is called with a random id
            Then an empty optional is returned
            """)
    @Test
    void test7() {
        var bookOptional = bookRepository.findById(Fixtures.getRandomUUID());

        then(bookOptional)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the table is populated by one book
            When the findById method is called with the book's id
            Then an optional is returned with this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void test8() {
        var bookOptional = bookRepository.findById(Fixtures.Book.BOOK_UUID);

        then(bookOptional)
                .isNotNull()
                .isNotEmpty()
                .get()
                .extracting(BookEntity::id)
                .isEqualTo(Fixtures.Book.BOOK_UUID);
    }

    @DisplayName("""
            Given there is a valid request
            When the saveFromRequest method is called
            Then the persistence is success
            And the persisted book is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test9() {
        var request = Fixtures.Book.getValidBookCreationRequest();
        given(BookEntity.validRequestOrThrow(request))
                .isEqualTo(request);

        var persistedBook = bookRepository.saveFromRequest(request);

        then(persistedBook)
                .extracting(BookEntity::id)
                .isNotNull();
    }


    @DisplayName("""
            Given there is a valid request
            But there is no persisted author
            When the saveFromRequest method is called
            Then the persistence is failed
            And a NoEntityFoundException is thrown
            """)
    @Test
    void test10() {
        var request = Fixtures.Book.getValidBookCreationRequest();
        given(BookEntity.validRequestOrThrow(request))
                .isEqualTo(request);

        var thrown = catchThrowable(() -> bookRepository.saveFromRequest(request));

        then(thrown)
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .isInstanceOf(DomainException.class)
                .hasMessage("Author is not found with " + Fixtures.Author.AUTHOR_UUID);
    }
}
