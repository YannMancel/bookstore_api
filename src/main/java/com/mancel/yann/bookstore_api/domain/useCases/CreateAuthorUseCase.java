package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;

public record CreateAuthorUseCase(TransactionDelegate transactionDelegate, AuthorRepository authorRepository)
        implements CreateUseCase<AuthorCreationRequest, AuthorEntity> {

    @Override
    public AuthorEntity execute(AuthorCreationRequest request) throws DomainException {
        return transactionDelegate.executeIntoTransaction(
                () -> {
                    AuthorEntity.validRequestOrThrow(request);
                    return authorRepository.saveFromRequest(request);});
    }
}
