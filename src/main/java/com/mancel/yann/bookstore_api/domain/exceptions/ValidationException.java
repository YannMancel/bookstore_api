package com.mancel.yann.bookstore_api.domain.exceptions;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
