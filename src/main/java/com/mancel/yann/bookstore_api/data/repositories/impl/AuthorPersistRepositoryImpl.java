package com.mancel.yann.bookstore_api.data.repositories.impl;

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

    @Autowired
    public AuthorPersistRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(AuthorModel.class);
    }

    @Override
    @Transactional
    public AuthorEntity save(AuthorEntity transientEntity) {
        try {
            var transientAuthor = AuthorModel.getBuilder()
                    .setEmail(transientEntity.email())
                    .setFirstName(transientEntity.firstName())
                    .setLastName(transientEntity.lastName())
                    .build();
            entityManager.persist(transientAuthor);
            return transientAuthor.getAuthorEntity();
        } catch (Exception exception) {
            throw new UnknownException(exception.getMessage(), exception);
        }
    }
}
