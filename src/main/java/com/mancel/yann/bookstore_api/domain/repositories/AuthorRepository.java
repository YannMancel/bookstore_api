package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends PersistRepository<AuthorEntity> {

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findById(UUID id);
}
