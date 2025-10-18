package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.entities.Book;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(UUID id);

    Book save(Book entity);
}
