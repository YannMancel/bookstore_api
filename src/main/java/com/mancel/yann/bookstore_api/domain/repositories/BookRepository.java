package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.data.models.BookModel;
import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends PersistRepository<BookCreationRequest, BookEntity> {

    List<BookModel> findAll();

    List<BookModel> findAllByAuthorId(UUID authorId);

    List<BookModel> findAllByTitleContaining(String subtitle);

    Optional<BookModel> findById(UUID id);

    BookModel save(BookModel entity);
}
