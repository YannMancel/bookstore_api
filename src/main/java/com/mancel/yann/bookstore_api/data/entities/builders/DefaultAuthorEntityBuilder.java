package com.mancel.yann.bookstore_api.data.entities.builders;

import com.mancel.yann.bookstore_api.data.entities.AuthorEntity;

import java.util.UUID;

public class DefaultAuthorEntityBuilder implements AuthorEntity.Builder {

    private final AuthorEntity authorEntity;

    public DefaultAuthorEntityBuilder() {
        authorEntity = new AuthorEntity();
    }

    @Override
    public AuthorEntity.Builder setId(UUID id) {
        authorEntity.setId(id);
        return this;
    }

    @Override
    public AuthorEntity.Builder setEmail(String email) {
        authorEntity.setEmail(email);
        return this;
    }

    @Override
    public AuthorEntity.Builder setFirstName(String firstName) {
        authorEntity.setFirstName(firstName);
        return this;
    }

    @Override
    public AuthorEntity.Builder setLastName(String lastName) {
        authorEntity.setLastName(lastName);
        return this;
    }

    @Override
    public AuthorEntity build() {
        return authorEntity;
    }
}
