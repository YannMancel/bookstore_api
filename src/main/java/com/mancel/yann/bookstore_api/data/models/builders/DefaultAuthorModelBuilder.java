package com.mancel.yann.bookstore_api.data.models.builders;

import com.mancel.yann.bookstore_api.data.models.AuthorModel;

import java.util.UUID;

public class DefaultAuthorModelBuilder implements AuthorModel.Builder {
    private final AuthorModel authorModel;

    public DefaultAuthorModelBuilder() {
        authorModel = new AuthorModel();
    }

    @Override
    public AuthorModel.Builder setId(UUID id) {
        authorModel.setId(id);
        return this;
    }

    @Override
    public AuthorModel.Builder setEmail(String email) {
        authorModel.setEmail(email);
        return this;
    }

    @Override
    public AuthorModel.Builder setFirstName(String firstName) {
        authorModel.setFirstName(firstName);
        return this;
    }

    @Override
    public AuthorModel.Builder setLastName(String lastName) {
        authorModel.setLastName(lastName);
        return this;
    }

    @Override
    public AuthorModel build() {
        return authorModel;
    }
}
