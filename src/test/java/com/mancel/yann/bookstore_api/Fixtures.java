package com.mancel.yann.bookstore_api;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.data.models.BookModel;

import java.util.UUID;

public class Fixtures {

    public static UUID getRandomUUID() {
        return UUID.randomUUID();
    }

    public static class Author {
        public static final UUID AUTHOR_UUID = UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");

        public static AuthorCreationRequest getValidAuthorCreationRequest() {
            return new AuthorCreationRequest(
                    "john.doe@gmail.com",
                    "John",
                    "Doe");
        }

        public static AuthorCreationRequest getInvalidAuthorCreationRequest(boolean hasNullFirstName,
                                                                            boolean hasNullLastName) {
            var request = getValidAuthorCreationRequest();
            return new AuthorCreationRequest(
                    request.email(),
                    hasNullFirstName ? null : request.firstName(),
                    hasNullLastName ? null : request.lastName());
        }

        public static AuthorModel getTransientAuthorModel() {
            var request = getValidAuthorCreationRequest();
            return new AuthorModel(request);
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

        public static BookModel getTransientBookModel(AuthorEntity author) {
            var authorModel = new AuthorModel();
            authorModel.setId(author.id());
            authorModel.setEmail(author.email());
            authorModel.setFirstName(author.firstName());
            authorModel.setLastName(author.lastName());
            return new BookModel(
                    "Berserk",
                    authorModel);
        }
    }
}
