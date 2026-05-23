package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.domain.models.AuthorModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends PersistRepository<AuthorModel> {

    List<AuthorModel> findAll();

    Optional<AuthorModel> findById(UUID id);
}
