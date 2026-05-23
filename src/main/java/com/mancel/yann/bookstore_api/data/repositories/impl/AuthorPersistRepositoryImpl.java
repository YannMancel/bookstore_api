package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.data.mappers.DataMapper;
import com.mancel.yann.bookstore_api.data.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.data.repositories.AuthorPersistRepository;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AuthorPersistRepositoryImpl implements AuthorPersistRepository {

    private final EntityManager entityManager;

    private final DataMapper<AuthorModel, AuthorEntity> authorDataMapper;

    @Autowired
    public AuthorPersistRepositoryImpl(
            JpaContext context,
            DataMapper<AuthorModel, AuthorEntity> authorDataMapper
    ) {
        this.entityManager = context.getEntityManagerByManagedType(AuthorEntity.class);
        this.authorDataMapper = authorDataMapper;
    }

    @Override
    @Transactional
    public AuthorModel save(AuthorModel transientModel) {
        try {
            var transientAuthorEntity = authorDataMapper.toTransientEntity(transientModel);
            entityManager.persist(transientAuthorEntity);
            return authorDataMapper.toPersistedModel(transientAuthorEntity);
        } catch (Exception exception) {
            throw new UnknownException(exception.getMessage(), exception);
        }
    }
}
