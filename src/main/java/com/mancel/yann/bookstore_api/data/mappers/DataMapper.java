package com.mancel.yann.bookstore_api.data.mappers;

public interface DataMapper<M, E> {

    M toPersistedModel(E persistedEntity);

    E toTransientEntity(M transientModel);
}
