package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;

public record CreateAuthorUseCase(TransactionDelegate transactionDelegate,
                                  AuthorRepository authorRepository) implements CreateUseCase<AuthorCreationRequest, AuthorEntity> {

    @Override
    public AuthorEntity execute(AuthorCreationRequest request) {
        return transactionDelegate.execute(() -> {
            AuthorEntity.validRequestOrThrow(request);
            return authorRepository.saveFromRequest(request);
        });
    }
}
