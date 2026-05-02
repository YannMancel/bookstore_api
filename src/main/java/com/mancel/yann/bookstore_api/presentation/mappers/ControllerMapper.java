package com.mancel.yann.bookstore_api.presentation.mappers;

public interface ControllerMapper<I, E, O> {
    O toResponse(E entity);

    E toTransientEntity(I request);
}
