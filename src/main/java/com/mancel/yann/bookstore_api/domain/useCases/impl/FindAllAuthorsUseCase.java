package com.mancel.yann.bookstore_api.domain.useCases.impl;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.FindAllUseCase;

import java.util.List;

public record FindAllAuthorsUseCase(AuthorRepository authorRepository) implements FindAllUseCase<AuthorEntity> {

    @Override
    public List<AuthorEntity> execute() {
        return authorRepository.findAll();
    }
}
