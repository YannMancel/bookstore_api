package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.domain.requests.Request;


public interface PersistRepository<T, R extends Request> {

    T saveFromRequest(R request);
}
