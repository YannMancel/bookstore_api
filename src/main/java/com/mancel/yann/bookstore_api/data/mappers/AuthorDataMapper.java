package com.mancel.yann.bookstore_api.data.mappers;

import com.mancel.yann.bookstore_api.data.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import org.springframework.util.Assert;

public class AuthorDataMapper implements DataMapper<AuthorModel, AuthorEntity> {

    @Override
    public AuthorModel toPersistedModel(AuthorEntity persistedEntity) {
        Assert.notNull(persistedEntity.getId(), "Persisted author entity's id must be not null.");
        return new AuthorModel(
                persistedEntity.getId(),
                persistedEntity.getEmail(),
                persistedEntity.getFirstName(),
                persistedEntity.getLastName());
    }

    @Override
    public AuthorEntity toTransientEntity(AuthorModel transientModel) {
        Assert.isNull(transientModel.id(), "Transient author model's id must be null.");
        return AuthorEntity.getBuilder()
                .setEmail(transientModel.email())
                .setFirstName(transientModel.firstName())
                .setLastName(transientModel.lastName())
                .build();
    }
}
