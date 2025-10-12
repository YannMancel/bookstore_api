package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.entities.Author;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface AuthorRepository {

    List<Author> findAll();

    Optional<Author> findById(UUID id);

    Author save(Author entity);
}
