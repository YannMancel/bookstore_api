package com.mancel.yann.bookstore_api.domain.entities;

import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;

import java.util.HashSet;
import java.util.UUID;

public record AuthorEntity(UUID id, String email, String firstName, String lastName) {

    public static AuthorCreationRequest validRequestOrThrow(AuthorCreationRequest request) {
        var errors = new HashSet<String>();
        if (request.firstName() == null) errors.add("First name is required.");
        if (request.lastName() == null) errors.add("Last name is required.");

        if (errors.isEmpty()) return request;

        var builder = new StringBuilder("Invalid request: ");
        for (var error : errors) {
            builder.append(error);
        }
        throw new ValidationException(builder.toString());
    }
}
