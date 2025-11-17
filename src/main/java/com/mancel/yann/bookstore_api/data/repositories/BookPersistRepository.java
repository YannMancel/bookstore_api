package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.repositories.PersistRepository;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;

public interface BookPersistRepository extends PersistRepository<BookCreationRequest, BookEntity> {
}
