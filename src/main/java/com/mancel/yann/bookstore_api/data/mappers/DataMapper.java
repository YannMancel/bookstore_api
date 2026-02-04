package com.mancel.yann.bookstore_api.data.mappers;

public interface DataMapper<E, M> {

    E toPersistedEntity(M persistedModel);

    M toTransientModel(E transientEntity);
}
