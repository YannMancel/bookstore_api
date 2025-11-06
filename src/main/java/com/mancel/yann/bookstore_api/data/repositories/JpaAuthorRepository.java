package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface JpaAuthorRepository extends AuthorRepository, AuthorPersistRepository, Repository<AuthorModel, UUID> {

    @Override
    @Query(value = "select m from AuthorModel m")
    List<AuthorEntity> findAll();

    @Override
    @Query(value = "select m from AuthorModel m where m.id=:id")
    Optional<AuthorEntity> findById(UUID id);
}
