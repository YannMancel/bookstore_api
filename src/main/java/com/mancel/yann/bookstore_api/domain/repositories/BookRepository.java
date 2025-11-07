package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends PersistRepository<BookCreationRequest, BookEntity> {

    List<BookEntity> findAll();

    List<BookEntity> findAllByAuthorId(UUID authorId);

    List<BookEntity> findAllByTitleContaining(String subtitle);

    Optional<BookEntity> findById(UUID id);
}
