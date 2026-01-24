package com.mancel.yann.bookstore_api.presentation.mappers;

public interface Mapper<I, E, O> {
    O toResponse(E entity);

    E toTransientEntity(I request);
}
