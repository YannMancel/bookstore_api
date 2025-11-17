package com.mancel.yann.bookstore_api.domain.exceptions;

public final class NoEntityFoundException extends DomainException {

    public NoEntityFoundException(String message) {
        super(message);
    }
}
