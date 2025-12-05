package com.mancel.yann.bookstore_api.domain.exceptions;

public final class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
