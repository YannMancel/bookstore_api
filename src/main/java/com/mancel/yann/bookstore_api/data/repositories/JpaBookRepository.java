package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.domain.repositories.BookRepository;
import com.mancel.yann.bookstore_api.entities.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaBookRepository extends BookRepository, ListCrudRepository<Book, UUID> {

    @Override
    @Query(value = "select b from Book b where b.author.id=:authorId")
    List<Book> findAllByAuthorId(UUID authorId);
}
