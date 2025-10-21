package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.entities.Author;

public record CreateAuthorUseCase(AuthorRepository authorRepository) {

    public Author execute(AuthorCreationRequest authorCreationRequest) throws ValidationException {
        authorCreationRequest.validOrThrow();
        var author = authorCreationRequest.convertToAuthor();
        return authorRepository.save(author);
    }
}
