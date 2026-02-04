package com.mancel.yann.bookstore_api.data.mappers;

import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import org.springframework.util.Assert;

public class AuthorDataMapper implements DataMapper<AuthorEntity, AuthorModel> {

    @Override
    public AuthorEntity toPersistedEntity(AuthorModel persistedModel) {
        Assert.notNull(persistedModel.getId(), "Persisted author's id must be not null.");
        return new AuthorEntity(
                persistedModel.getId(),
                persistedModel.getEmail(),
                persistedModel.getFirstName(),
                persistedModel.getLastName());
    }

    @Override
    public AuthorModel toTransientModel(AuthorEntity transientEntity) {
        Assert.isNull(transientEntity.id(), "Transient author's id must be null.");
        return AuthorModel.getBuilder()
                .setEmail(transientEntity.email())
                .setFirstName(transientEntity.firstName())
                .setLastName(transientEntity.lastName())
                .build();
    }
}
