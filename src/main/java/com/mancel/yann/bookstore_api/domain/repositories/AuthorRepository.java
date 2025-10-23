package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.entities.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends PersistRepository<Author, AuthorCreationRequest> {

    List<Author> findAll();

    Optional<Author> findById(UUID id);

    Author save(Author entity);
}
