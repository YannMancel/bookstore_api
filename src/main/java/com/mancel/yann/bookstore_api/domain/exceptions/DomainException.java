package com.mancel.yann.bookstore_api.domain.exceptions;

public abstract sealed class DomainException extends RuntimeException permits ValidationException {

    public DomainException(String message) {
        super(message);
    }
}
