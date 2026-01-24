package com.mancel.yann.bookstore_api.domain.useCases.impl;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;

public record SaveAuthorUseCase(TransactionDelegate transactionDelegate,
                                AuthorRepository authorRepository) implements SaveUseCase<AuthorEntity> {

    @Override
    public AuthorEntity execute(AuthorEntity transientEntity) {
        return transactionDelegate.execute(() -> {
            transientEntity.validOrThrow();
            return authorRepository.save(transientEntity);
        });
    }
}
