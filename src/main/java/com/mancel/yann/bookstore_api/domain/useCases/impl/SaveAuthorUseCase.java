package com.mancel.yann.bookstore_api.domain.useCases.impl;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;

public record SaveAuthorUseCase(TransactionDelegate transactionDelegate,
                                AuthorRepository authorRepository) implements SaveUseCase<AuthorCreationRequest, AuthorEntity> {

    @Override
    public AuthorEntity execute(AuthorCreationRequest request) {
        return transactionDelegate.execute(() -> {
            AuthorEntity.validRequestOrThrow(request);
            return authorRepository.saveFromRequest(request);
        });
    }
}
