package com.mancel.yann.bookstore_api;

import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.data.models.BookModel;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.dto.requests.BookCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.mappers.AuthorMapper;

public abstract class Fixtures {

    public static java.util.UUID getRandomUUID() {
        return java.util.UUID.randomUUID();
    }

    public static class Author {
        public static final java.util.UUID UUID = java.util.UUID.fromString("64f07a63-1c1c-415e-b2c7-6a54860e6083");
        public static final AuthorMapper MAPPER = new AuthorMapper();

        public static AuthorCreationRequestDto getValidCreationRequest() {
            return new AuthorCreationRequestDto("john.doe@gmail.com", "John", "Doe");
        }

        public static AuthorModel getTransientModel() {
            var request = getValidCreationRequest();
            return AuthorModel.getBuilder()
                    .setEmail(request.email())
                    .setFirstName(request.firstName())
                    .setLastName(request.lastName())
                    .build();
        }

        public static AuthorEntity getPersistedEntity() {
            var model = getTransientModel();
            return new AuthorEntity(
                    UUID,
                    model.getEmail(),
                    model.getFirstName(),
                    model.getLastName());
        }
    }

    public static class Book {
        public static final java.util.UUID UUID = java.util.UUID.fromString("1955a2d7-5367-4c63-8323-31ad9bd3db31");

        public static final String TITLE = "Horde";

        public static BookCreationRequestDto getValidCreationRequest() {
            return new BookCreationRequestDto("Berserk", Author.UUID);
        }

        public static BookModel getTransientBookModel() {
            var request = getValidCreationRequest();
            var authorModel = Author.getTransientModel();
            authorModel.setId(Author.UUID);
            return BookModel.getBuilder()
                    .setTitle(request.title())
                    .setAuthor(authorModel)
                    .build();
        }
    }
}
