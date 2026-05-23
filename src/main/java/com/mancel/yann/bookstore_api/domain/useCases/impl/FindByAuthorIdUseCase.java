package com.mancel.yann.bookstore_api.domain.useCases.impl;

import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.exceptions.EntityNotFoundException;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.FindByIdUseCase;

import java.text.MessageFormat;
import java.util.UUID;

public record FindByAuthorIdUseCase(AuthorRepository authorRepository) implements FindByIdUseCase<AuthorModel> {

    @Override
    public AuthorModel execute(UUID id) {
        return authorRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Author is not found with {0}", id.toString())));
    }
}
