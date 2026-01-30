package com.mancel.yann.bookstore_api.domain.useCases.impl;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.TransactionException;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;

public record SaveAuthorUseCase(TransactionDelegate transactionDelegate,
                                AuthorRepository authorRepository) implements SaveUseCase<AuthorEntity> {

    @Override
    public AuthorEntity execute(AuthorEntity transientEntity) {
        try {
            return transactionDelegate.execute(() -> {
                transientEntity.validOrThrow();
                return authorRepository.save(transientEntity);
            });
        } catch (DomainException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new TransactionException(exception.getMessage(), exception);
        }
    }
}
