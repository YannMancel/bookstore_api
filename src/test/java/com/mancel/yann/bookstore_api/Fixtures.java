package com.mancel.yann.bookstore_api;

import com.mancel.yann.bookstore_api.data.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.data.mappers.AuthorDataMapper;
import com.mancel.yann.bookstore_api.data.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.dto.requests.BookCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.mappers.AuthorControllerMapper;

import java.util.Set;

public abstract class Fixtures {

    public static java.util.UUID getRandomUUID() {
        return java.util.UUID.randomUUID();
    }

    public static class Author {
        public static final java.util.UUID UUID = java.util.UUID.fromString(
                "64f07a63-1c1c-415e-b2c7-6a54860e6083"
        );

        public static final java.util.UUID UUID_FOR_MULTIPLE_BOOKS = UUID;

        public static final java.util.UUID UUID_FOR_SINGLE_BOOK = java.util.UUID.fromString(
                "44e0e256-3572-40d8-9622-b39036a71363"
        );

        public static final AuthorDataMapper DATA_MAPPER = new AuthorDataMapper();

        public static final AuthorControllerMapper CONTROLLER_MAPPER = new AuthorControllerMapper();

        public static AuthorCreationRequestDto getValidCreationRequest() {
            return new AuthorCreationRequestDto(
                    "john.doe@gmail.com",
                    "John",
                    "Doe"
            );
        }

        public static AuthorModel getTransientModel() {
            var authorCreationRequest = getValidCreationRequest();
            return Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);
        }

        public static AuthorEntity getTransientEntity() {
            var transientAuthorModel = getTransientModel();
            return Author.DATA_MAPPER.toTransientEntity(transientAuthorModel);
        }

        public static AuthorEntity getPersistedEntity() {
            var transientAuthorEntity = getTransientEntity();
            transientAuthorEntity.setId(Author.UUID);
            return transientAuthorEntity;
        }

        public static AuthorModel getPersistedModel() {
            var transientAuthorModel = getTransientModel();
            return new AuthorModel(
                    UUID,
                    transientAuthorModel.email(),
                    transientAuthorModel.firstName(),
                    transientAuthorModel.lastName());
        }
    }

    public static class Book {
        public static final java.util.UUID UUID = java.util.UUID.fromString("1955a2d7-5367-4c63-8323-31ad9bd3db31");

        public static final String SUBTITLE = "Horde";

        public static BookCreationRequestDto getValidCreationRequest() {
            return new BookCreationRequestDto("Berserk", Author.UUID);
        }

        public static BookEntity getTransientEntity() {
            var bookCreationRequest =  getValidCreationRequest();
            var persistedAuthorEntity = Author.getPersistedEntity();
            return BookEntity.getBuilder()
                    .setTitle(bookCreationRequest.title())
                    .setAuthors(Set.of(persistedAuthorEntity))
                    .build();
        }
    }
}
