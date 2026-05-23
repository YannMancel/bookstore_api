package com.mancel.yann.bookstore_api.presentation.mappers;

public interface ControllerMapper<I, M, O> {
    O toResponse(M persistedModel);

    M toTransientModel(I request);
}
