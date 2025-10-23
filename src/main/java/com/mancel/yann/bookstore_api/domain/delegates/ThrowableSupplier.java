package com.mancel.yann.bookstore_api.domain.delegates;

import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;

@FunctionalInterface
public interface ThrowableSupplier<T> {
    T getOrThrow() throws DomainException;
}
