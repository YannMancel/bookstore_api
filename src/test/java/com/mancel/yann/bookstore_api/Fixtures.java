package com.mancel.yann.bookstore_api;

import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.entities.Author;
import com.mancel.yann.bookstore_api.entities.Book;

import java.util.UUID;

public class Fixtures {

    public static UUID getRandomUUID() {
        return UUID.randomUUID();
    }

    public static final UUID AUTHOR_UUID = UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");

    public static final UUID BOOK_UUID = UUID.fromString("1955a2d7-5367-4c63-8323-31ad9bd3db31");

    public static final String BOOK_SUBTITLE = "Horde";

    public static AuthorCreationRequest getInvalidAuthorCreationRequest() {
        return new AuthorCreationRequest(
                "john.doe@gmail.com",
                null, // firstName must be not null to be valid
                "Doe");
    }

    public static AuthorCreationRequest getValidAuthorCreationRequest() {
        return new AuthorCreationRequest(
                "john.doe@gmail.com",
                "John",
                "Doe");
    }

    public static Author getTransientAuthor() {
        return getValidAuthorCreationRequest().convertToAuthor();
    }

    public static Author getPersistedAuthor() {
        var transientAuthor = getTransientAuthor();
        transientAuthor.setId(AUTHOR_UUID);
        return transientAuthor;
    }

    public static Book getTransientBook(Author author) {
        return new Book(
                "Berserk",
                author);
    }
}
