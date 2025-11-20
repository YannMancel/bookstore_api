package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.domain.requests.Request;

public interface SaveUseCase<R extends Request, T> {

    T execute(R request);
}
