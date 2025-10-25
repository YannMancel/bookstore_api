package com.mancel.yann.bookstore_api.data.delegates;

import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.delegates.ThrowableSupplier;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import org.springframework.transaction.annotation.Transactional;

public class JpaTransactionDelegate implements TransactionDelegate {

    @Transactional
    @Override
    public <T> T executeIntoTransaction(ThrowableSupplier<T> throwableSupplier) throws DomainException {
        try {
            return throwableSupplier.getOrThrow();
        } catch (DomainException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new UnknownException(exception.getMessage(), exception);
        }
    }
}
