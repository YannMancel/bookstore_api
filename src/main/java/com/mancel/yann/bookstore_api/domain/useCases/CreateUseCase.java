package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.requests.Request;

public interface CreateUseCase<R extends Request, T> {

    T execute(R request) throws DomainException;
}
