package com.mancel.yann.bookstore_api.domain.useCases.impl;

import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.FindAllUseCase;

import java.util.List;

public record FindAllAuthorsUseCase(AuthorRepository authorRepository) implements FindAllUseCase<AuthorModel> {

    @Override
    public List<AuthorModel> execute() {
        return authorRepository.findAll();
    }
}
