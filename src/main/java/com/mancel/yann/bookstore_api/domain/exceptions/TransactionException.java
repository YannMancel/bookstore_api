package com.mancel.yann.bookstore_api.domain.exceptions;

public final class TransactionException extends DomainException {

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
