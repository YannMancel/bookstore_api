package com.mancel.yann.bookstore_api.mocks;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;

import java.util.function.Supplier;

public class FakeTransactionDelegate implements TransactionDelegate {

    @Override
    public <T> T execute(Supplier<T> supplier) {
        return supplier.get();
    }
}
