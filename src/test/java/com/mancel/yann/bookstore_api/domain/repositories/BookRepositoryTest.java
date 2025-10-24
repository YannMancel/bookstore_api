package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.entities.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @DisplayName(
            """
            Given the books table is empty
            When the findAll method is called
            Then an empty book list is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindAllIsCalled_thenReturnsEmptyList() {
        var books = bookRepository.findAll();

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When the findAll method is called
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllIsCalled_thenReturnsAListContainingThisBook() {
        var books = bookRepository.findAll();

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(Book::getId)
                        .isEqualTo(Fixtures.BOOK_UUID);
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When the findAllByAuthorId method is called with the author's UUID of this book
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByAuthorIdIsCalled_thenReturnsAListContainingThisBook() {
        var books = bookRepository.findAllByAuthorId(Fixtures.AUTHOR_UUID);

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(Book::getId)
                        .isEqualTo(Fixtures.BOOK_UUID);
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When the findAllByAuthorId method is called with a random UUID
            Then an empty book list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByAuthorIdIsCalled_thenReturnsAnEmptyListIsReturned() {
        var books = bookRepository.findAllByAuthorId(Fixtures.getRandomUUID());

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When the findAllByTitleContaining method is called with a subtitle of book's title
            Then a book list is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByTitleContainingIsCalled_thenReturnsAListContainingThisBook() {
        var books = bookRepository.findAllByTitleContaining(Fixtures.BOOK_SUBTITLE);

        then(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(Book::getId)
                        .isEqualTo(Fixtures.BOOK_UUID);
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When the findAllByTitleContaining method is called with a random subtitle
            Then an empty book list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByTitleContainingIsCalled_thenReturnsAnEmptyListIsReturned() {
        var randomSubtitle = Fixtures.getRandomUUID().toString();

        var books = bookRepository.findAllByTitleContaining(randomSubtitle);

        then(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the books table is empty
            When the findById method is called with a random UUID
            Then an empty optional is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindByIdIsCalledWithRandomUUID_thenReturnsEmptyOptional() {
        var bookOptional = bookRepository.findById(Fixtures.getRandomUUID());

        then(bookOptional)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given the books table is populated by one book
            When the findById method is called with the book's UUID
            Then a not empty book optional is returned
            And it contains this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindByIdIsCalledWithBookUUID_thenReturnsBook() {
        var bookOptional = bookRepository.findById(Fixtures.BOOK_UUID);

        then(bookOptional)
                .isNotNull()
                .isNotEmpty()
                .get()
                    .extracting(Book::getId)
                        .isEqualTo(Fixtures.BOOK_UUID);
    }

    @DisplayName(
            """
            Given there is a persisted author
            And there is a transient book
            When the save method is called
            Then the persistence is success
            And the persisted book is return
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void givenTransientAuthor_whenSaveIsCalled_thenPersistenceIsSuccess() {
        var persistedAuthor = authorRepository
                .findById(Fixtures.AUTHOR_UUID)
                .orElseThrow();
        var transientBook = Fixtures.getTransientBook(persistedAuthor);
        given(transientBook)
                .extracting(Book::getId)
                    .isNull();

        var persistedBook = bookRepository.save(transientBook);

        then(transientBook)
                .isEqualTo(persistedBook)
                .extracting(Book::getId)
                    .isNotNull();
    }
}
