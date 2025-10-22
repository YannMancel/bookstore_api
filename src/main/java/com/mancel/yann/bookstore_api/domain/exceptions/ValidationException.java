package com.mancel.yann.bookstore_api.domain.exceptions;

public final class ValidationException extends DomainException {

    public ValidationException(String message) {
        super(message);
    }
}
