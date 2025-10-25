package com.mancel.yann.bookstore_api.domain.exceptions;

public final class UnknownException extends DomainException {

    public UnknownException(String message, Throwable cause) {
        super(message, cause);
    }
}
