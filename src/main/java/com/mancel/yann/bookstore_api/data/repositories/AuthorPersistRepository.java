package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.domain.repositories.PersistRepository;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.entities.Author;

public interface AuthorPersistRepository extends PersistRepository<Author, AuthorCreationRequest> { }
