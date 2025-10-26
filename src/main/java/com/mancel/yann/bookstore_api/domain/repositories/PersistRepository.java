package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.requests.Request;


public interface PersistRepository<R extends Request, T> {

    T saveFromRequest(R request) throws DomainException;
}
