package com.mancel.yann.bookstore_api.domain.useCases.impl;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.TransactionException;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;

public record SaveAuthorUseCase(TransactionDelegate transactionDelegate,
                                AuthorRepository authorRepository) implements SaveUseCase<AuthorModel> {

    @Override
    public AuthorModel execute(AuthorModel transientModel) {
        try {
            return transactionDelegate.execute(() -> {
                transientModel.validOrThrow();
                return authorRepository.save(transientModel);
            });
        } catch (DomainException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new TransactionException(exception.getMessage(), exception);
        }
    }
}
