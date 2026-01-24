package com.mancel.yann.bookstore_api.domain.useCases;

public interface SaveUseCase<T> {

    T execute(T transientEntity);
}
