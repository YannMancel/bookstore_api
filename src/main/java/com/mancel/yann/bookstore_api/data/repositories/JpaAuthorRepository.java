package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.data.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
@Transactional(readOnly = true)
public interface JpaAuthorRepository extends AuthorRepository, AuthorPersistRepository, Repository<AuthorEntity, UUID> {

    @Override
    @Query(value = "select a from AuthorEntity a")
    List<AuthorModel> findAll();

    @Override
    @Query(value = "select a from AuthorEntity a where a.id=:id")
    Optional<AuthorModel> findById(UUID id);
}
