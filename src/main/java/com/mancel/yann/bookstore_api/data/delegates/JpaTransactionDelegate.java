package com.mancel.yann.bookstore_api.data.delegates;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

public class JpaTransactionDelegate implements TransactionDelegate {

    @Transactional
    @Override
    public <T> T execute(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (DomainException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new UnknownException(exception.getMessage(), exception);
        }
    }
}
