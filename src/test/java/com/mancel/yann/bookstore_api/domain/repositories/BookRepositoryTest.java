package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.entities.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @DisplayName(
            """
            Given table is empty
            When findAll method is called
            Then empty list is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindAllIsCalled_thenReturnsEmptyList() {
        var books = bookRepository.findAll();

        assertThat(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is populated by one book
            When findAll method is called
            Then a list containing this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllIsCalled_thenReturnsAListContainingThisBook() {
        var books = bookRepository.findAll();

        assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @DisplayName(
            """
            Given table is populated by one book
            When findAllByAuthorId method is called with author's UUID of this book
            Then a list containing this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByAuthorIdIsCalled_thenReturnsAListContainingThisBook() {
        var uuid = UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");
        var books = bookRepository.findAllByAuthorId(uuid);

        assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @DisplayName(
            """
            Given table is populated by one book
            When findAllByAuthorId method is called with a random UUID
            Then an empty list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByAuthorIdIsCalled_thenReturnsAnEmptyListIsReturned() {
        var uuid = UUID.randomUUID();
        var books = bookRepository.findAllByAuthorId(uuid);

        assertThat(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is populated by one book
            When findAllByTitleContaining method is called with a subtitle of book's title
            Then a list containing this book
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByTitleContainingIsCalled_thenReturnsAListContainingThisBook() {
        var books = bookRepository.findAllByTitleContaining("Horde");

        assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @DisplayName(
            """
            Given table is populated by one book
            When findAllByTitleContaining method is called with a random subtitle
            Then an empty list is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindAllByTitleContainingIsCalled_thenReturnsAnEmptyListIsReturned() {
        var randomSubtitle = UUID.randomUUID().toString();
        var books = bookRepository.findAllByTitleContaining(randomSubtitle);

        assertThat(books)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is empty
            When findById method is called with a random UUID
            Then empty optional is returned
            """)
    @Test
    void givenTableIsEmpty_whenFindByIdIsCalledWithRandomUUID_thenReturnsEmptyOptional() {
        var uuid = UUID.randomUUID();
        var bookOptional = bookRepository.findById(uuid);

        assertThat(bookOptional)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName(
            """
            Given table is populated by one book
            When findById method is called with the book's UUID
            Then this book is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author_and_one_book.sql"})
    void givenTableIsPopulatedByOneBook_whenFindByIdIsCalledWithBookUUID_thenReturnsBook() {
        var uuid = UUID.fromString("1955a2d7-5367-4c63-8323-31ad9bd3db31");
        var bookOptional = bookRepository.findById(uuid);

        assertThat(bookOptional)
                .isNotNull()
                .isNotEmpty();
    }

    @DisplayName(
            """
            Given an author is persisted
            And a transient book
            When save method is called
            Then the persistence is success
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void givenTransientAuthor_whenSaveIsCalled_thenPersistenceIsSuccess() {
        var uuid = UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");
        var persistedAuthor = authorRepository
                .findById(uuid)
                .orElseThrow();

        var transientBook = new Book(
                "Berserk",
                persistedAuthor);
        assertThat(transientBook)
                .extracting(Book::getId)
                .isNull();

        var persistedBook = bookRepository.save(transientBook);

        assertThat(transientBook)
                .isEqualTo(persistedBook)
                .extracting(Book::getId)
                    .isNotNull();
    }
}
