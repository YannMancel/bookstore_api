package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.domain.repositories.BookRepository;
import com.mancel.yann.bookstore_api.data.models.BookModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaBookRepository extends BookRepository, ListCrudRepository<BookModel, UUID> {

    @Override
    @Query(value = "select m from BookModel m where m.author.id=:authorId")
    List<BookModel> findAllByAuthorId(UUID authorId);
}
