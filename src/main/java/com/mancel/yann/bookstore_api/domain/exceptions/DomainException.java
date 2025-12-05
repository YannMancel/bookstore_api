package com.mancel.yann.bookstore_api.domain.exceptions;

public abstract sealed class DomainException extends RuntimeException permits EntityNotFoundException, UnknownException, ValidationException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
