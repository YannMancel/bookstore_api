package com.mancel.yann.bookstore_api.domain.useCases;

import java.util.UUID;

public interface FindByIdUseCase<T> {

    T execute(UUID id);
}
