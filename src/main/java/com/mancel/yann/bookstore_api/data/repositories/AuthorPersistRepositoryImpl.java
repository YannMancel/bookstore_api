package com.mancel.yann.bookstore_api.data.repositories;

import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.entities.Author;
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
        this.entityManager = context.getEntityManagerByManagedType(Author.class);
    }

    @Override
    @Transactional
    public Author saveFromRequest(AuthorCreationRequest request) {
        var transientAuthor = request.convertToAuthor();
        entityManager.persist(transientAuthor);
        return transientAuthor;
    }
}
