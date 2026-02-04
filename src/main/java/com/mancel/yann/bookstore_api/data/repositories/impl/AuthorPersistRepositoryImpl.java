package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.data.mappers.DataMapper;
import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.data.repositories.AuthorPersistRepository;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AuthorPersistRepositoryImpl implements AuthorPersistRepository {

    private final EntityManager entityManager;

    private final DataMapper<AuthorEntity, AuthorModel> authorDataMapper;

    @Autowired
    public AuthorPersistRepositoryImpl(JpaContext context,
                                       DataMapper<AuthorEntity, AuthorModel> authorDataMapper) {
        this.entityManager = context.getEntityManagerByManagedType(AuthorModel.class);
        this.authorDataMapper = authorDataMapper;
    }

    @Override
    @Transactional
    public AuthorEntity save(AuthorEntity transientEntity) {
        try {
            var transientAuthorModel = authorDataMapper.toTransientModel(transientEntity);
            entityManager.persist(transientAuthorModel);
            return authorDataMapper.toPersistedEntity(transientAuthorModel);
        } catch (Exception exception) {
            throw new UnknownException(exception.getMessage(), exception);
        }
    }
}
