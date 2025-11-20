package com.mancel.yann.bookstore_api.domain.useCases;

import java.util.List;

public interface FindAllUseCase<T> {

    List<T> execute();
}
