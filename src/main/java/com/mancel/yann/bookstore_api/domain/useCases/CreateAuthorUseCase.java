package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.entities.Author;

public record CreateAuthorUseCase(AuthorRepository authorRepository,
                                  TransactionDelegate transactionDelegate) {

    public Author execute(AuthorCreationRequest authorCreationRequest) throws DomainException {
        return transactionDelegate.executeIntoTransaction(
                () -> {
                    authorCreationRequest.validOrThrow();
                    var author = authorCreationRequest.convertToAuthor();
                    return authorRepository.save(author);});
    }
}
