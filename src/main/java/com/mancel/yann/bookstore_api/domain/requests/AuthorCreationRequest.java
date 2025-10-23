package com.mancel.yann.bookstore_api.domain.requests;

import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.entities.Author;

public record AuthorCreationRequest(String email,
                                    String firstName,
                                    String lastName) {

    public Author convertToAuthor() {
        return new Author(
                email,
                firstName,
                lastName);
    }

    public void validOrThrow() throws ValidationException {
        if (firstName == null || lastName == null) {
            var builder = new StringBuilder("Invalid entity: ");
            if (firstName == null) builder.append("The request's firstName is null.");
            if (lastName == null) builder.append("The request's lastName is null.");
            throw new ValidationException(builder.toString());
        }
    }
}
