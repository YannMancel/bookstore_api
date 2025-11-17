package com.mancel.yann.bookstore_api.domain.delegates;

import java.util.function.Supplier;

public interface TransactionDelegate {

    <T> T execute(Supplier<T> supplier);
}
