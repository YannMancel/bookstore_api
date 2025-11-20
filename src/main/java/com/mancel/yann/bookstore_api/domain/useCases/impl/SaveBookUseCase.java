package com.mancel.yann.bookstore_api.domain.useCases.impl;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.repositories.BookRepository;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;

public record SaveBookUseCase(TransactionDelegate transactionDelegate,
                              BookRepository bookRepository) implements SaveUseCase<BookCreationRequest, BookEntity> {

    @Override
    public BookEntity execute(BookCreationRequest request) {
        return transactionDelegate.execute(() -> {
            BookEntity.validRequestOrThrow(request);
            return bookRepository.saveFromRequest(request);
        });
    }
}
