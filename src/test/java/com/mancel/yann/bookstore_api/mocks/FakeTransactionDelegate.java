package com.mancel.yann.bookstore_api.mocks;

import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.delegates.ThrowableSupplier;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;

public class FakeTransactionDelegate implements TransactionDelegate {

    @Override
    public <T> T executeIntoTransaction(ThrowableSupplier<T> throwableSupplier) throws DomainException {
        return throwableSupplier.getOrThrow();
    }
}
