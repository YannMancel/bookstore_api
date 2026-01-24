package com.mancel.yann.bookstore_api.domain.repositories;

public interface PersistRepository<T> {

    T save(T transientEntity);
}
