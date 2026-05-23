package com.mancel.yann.bookstore_api.domain.models;

import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;

import java.util.HashSet;
import java.util.UUID;

public record AuthorModel(UUID id, String email, String firstName, String lastName) {

    public static final int EMAIL_LENGTH = 100;
    public static final int FIRST_NAME_LENGTH = 50;
    public static final int LAST_NAME_LENGTH = 50;

    public AuthorModel(String email, String firstName, String lastName) {
        this(null, email, firstName, lastName);
    }

    public void validOrThrow() {
        var errors = new HashSet<String>();
        if (firstName() == null) errors.add("First name is required.");
        if (lastName() == null) errors.add("Last name is required.");

        if (errors.isEmpty()) return;

        var builder = new StringBuilder("Invalid request: ");
        for (var error : errors) {
            builder.append(error);
        }
        throw new ValidationException(builder.toString());
    }
}
