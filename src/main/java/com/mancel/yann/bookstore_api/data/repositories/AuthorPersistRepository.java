package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.repositories.PersistRepository;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;

public interface AuthorPersistRepository extends PersistRepository<AuthorCreationRequest, AuthorEntity> { }
