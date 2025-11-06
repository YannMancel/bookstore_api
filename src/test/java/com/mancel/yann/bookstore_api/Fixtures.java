package com.mancel.yann.bookstore_api;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.data.models.BookModel;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;

import java.util.UUID;

public class Fixtures {

    public static UUID getRandomUUID() {
        return UUID.randomUUID();
    }

    public static class Author {
        public static final UUID AUTHOR_UUID = UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");

        public static AuthorCreationRequest getValidAuthorCreationRequest() {
            return new AuthorCreationRequest("john.doe@gmail.com", "John", "Doe");
        }

        public static AuthorModel getTransientAuthorModel() {
            var request = getValidAuthorCreationRequest();
            return AuthorModel.getBuilder()
                    .setEmail(request.email())
                    .setFirstName(request.firstName())
                    .setLastName(request.lastName())
                    .build();
        }

        public static AuthorEntity getPersistedAuthorEntity() {
            var model = getTransientAuthorModel();
            return new AuthorEntity(
                    AUTHOR_UUID,
                    model.getEmail(),
                    model.getFirstName(),
                    model.getLastName());
        }
    }

    public static class Book {
        public static final UUID BOOK_UUID = UUID.fromString("1955a2d7-5367-4c63-8323-31ad9bd3db31");

        public static final String BOOK_SUBTITLE = "Horde";

        public static BookCreationRequest getValidBookCreationRequest() {
            return new BookCreationRequest("Berserk", Author.AUTHOR_UUID);
        }
        public static BookModel getTransientBookModel() {
            var request = getValidBookCreationRequest();
            var authorModel = Author.getTransientAuthorModel();
            authorModel.setId(Author.AUTHOR_UUID);
            return BookModel.getBuilder()
                    .setTitle(request.title())
                    .setAuthor(authorModel)
                    .build();
        }
    }
}
