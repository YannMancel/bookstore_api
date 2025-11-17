package com.mancel.yann.bookstore_api.data.repositories.impl;

import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.data.models.BookModel;
import com.mancel.yann.bookstore_api.data.repositories.BookPersistRepository;
import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Repository
public class BookPersistRepositoryImpl implements BookPersistRepository {

    private final EntityManager entityManager;

    @Autowired
    public BookPersistRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(BookModel.class);
    }


    @Override
    @Transactional
    public BookEntity saveFromRequest(BookCreationRequest request) {
        try {
            var persistedAuthor = entityManager.find(AuthorModel.class, request.authorId());

            if (persistedAuthor == null) {
                throw new IllegalArgumentException(
                        MessageFormat.format("Author model is not found with {0}", request.authorId().toString()));
            }

            var transientBook = BookModel.getBuilder()
                    .setTitle(request.title())
                    .setAuthor(persistedAuthor)
                    .build();
            entityManager.persist(transientBook);
            return transientBook.getBookEntity();
        } catch (DomainException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new UnknownException(exception.getMessage(), exception);
        }
    }
}
