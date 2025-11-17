package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.repositories.BookRepository;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;

public record CreateBookUseCase(TransactionDelegate transactionDelegate,
                                BookRepository bookRepository) implements CreateUseCase<BookCreationRequest, BookEntity> {

    @Override
    public BookEntity execute(BookCreationRequest request) {
        return transactionDelegate.execute(() -> {
            BookEntity.validRequestOrThrow(request);
            return bookRepository.saveFromRequest(request);
        });
    }
}
