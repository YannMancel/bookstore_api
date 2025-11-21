package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.repositories.BookRepository;
import com.mancel.yann.bookstore_api.data.models.BookModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
@Transactional(readOnly = true)
public interface JpaBookRepository extends BookRepository, BookPersistRepository, Repository<BookModel, UUID> {

    @Override
    @Query(value = "select " +
                        "m.id, " +
                        "m.title, " +
                        "m.author.id as authorId, " +
                        "m.author.email as authorEmail, " +
                        "m.author.firstName as authorFirstName, " +
                        "m.author.lastName as authorLastName " +
                    "from " +
                        "BookModel m")
    List<BookEntity> findAll();

    @Override
    @Query(value = "select " +
                        "m.id, " +
                        "m.title, " +
                        "m.author.id as authorId, " +
                        "m.author.email as authorEmail, " +
                        "m.author.firstName as authorFirstName, " +
                        "m.author.lastName as authorLastName " +
                    "from " +
                        "BookModel m " +
                    "where " +
                        "m.author.id=:authorId")
    List<BookEntity> findAllByAuthorId(UUID authorId);

    @Override
    @Query(value = "select " +
                        "m.id, " +
                        "m.title, " +
                        "m.author.id as authorId, " +
                        "m.author.email as authorEmail, " +
                        "m.author.firstName as authorFirstName, " +
                        "m.author.lastName as authorLastName " +
                    "from " +
                        "BookModel m " +
                    "where " +
                        "m.title like %:subtitle%")
    List<BookEntity> findAllByTitleContaining(String subtitle);

    @Override
    @Query(value = "select " +
                        "m.id, " +
                        "m.title, " +
                        "m.author.id as authorId, " +
                        "m.author.email as authorEmail, " +
                        "m.author.firstName as authorFirstName, " +
                        "m.author.lastName as authorLastName " +
                    "from " +
                        "BookModel m " +
                    "where " +
                        "m.id=:id")
    Optional<BookEntity> findById(UUID id);
}
