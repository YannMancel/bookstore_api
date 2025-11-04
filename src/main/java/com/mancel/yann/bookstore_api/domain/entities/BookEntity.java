package com.mancel.yann.bookstore_api.domain.entities;

import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;

import java.util.HashSet;
import java.util.UUID;

public record BookEntity(UUID id, String title, AuthorEntity author) {

    public static BookCreationRequest validRequestOrThrow(BookCreationRequest request) throws ValidationException {
        var errors = new HashSet<String>();
        if (request.title() == null) errors.add("Title is required.");
        if (request.authorId() == null) errors.add("Author id is required.");

        if (errors.isEmpty()) return request;

        var builder = new StringBuilder("Invalid request: ");
        for (var error : errors) {
            builder.append(error);
        }
        throw new ValidationException(builder.toString());
    }
}
