package com.mancel.yann.bookstore_api.domain.delegates;

import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;

public interface TransactionDelegate {

    <T> T executeIntoTransaction(ThrowableSupplier<T> throwableSupplier) throws DomainException;
}
