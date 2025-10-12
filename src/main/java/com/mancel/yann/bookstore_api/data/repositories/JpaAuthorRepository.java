package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.entities.Author;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaAuthorRepository extends AuthorRepository, ListCrudRepository<Author, UUID> {
}
