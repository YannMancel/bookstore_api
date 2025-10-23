package com.mancel.yann.bookstore_api.data.delegates;

import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.delegates.ThrowableSupplier;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import org.springframework.transaction.annotation.Transactional;

public class JpaTransactionDelegate implements TransactionDelegate {

    @Transactional
    @Override
    public <T> T executeIntoTransaction(ThrowableSupplier<T> throwableSupplier) throws DomainException {
        return throwableSupplier.getOrThrow();
    }
}
