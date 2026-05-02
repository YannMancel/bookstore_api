package com.mancel.yann.bookstore_api.data.mappers;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

class AuthorDataMapperTest {

    @DisplayName("Should convert persisted author model to persisted author entity with success")
    @Test
    void test1() {
        var transientModel = Fixtures.Author.getTransientModel();
        var persistedModel = AuthorModel.getBuilder()
                .setId(Fixtures.Author.UUID)
                .setEmail(transientModel.getEmail())
                .setFirstName(transientModel.getFirstName())
                .setLastName(transientModel.getLastName())
                .build();
        given(persistedModel)
                .isNotNull()
                .extracting(AuthorModel::getId)
                    .isNotNull();

        var persistedEntity = Fixtures.Author.DATA_MAPPER.toPersistedEntity(persistedModel);

        then(persistedEntity)
                .isNotNull()
                .extracting(AuthorEntity::id)
                    .isNotNull();
    }

    @DisplayName("Should convert transient author entity to transient author model with success")
    @Test
    void test2() {
        var request = Fixtures.Author.getValidCreationRequest();
        var transientEntity = Fixtures.Author.CONTROLLER_MAPPER.toTransientEntity(request);

        var transientModel = Fixtures.Author.DATA_MAPPER.toTransientModel(transientEntity);

        then(transientModel)
                .isNotNull()
                .extracting(AuthorModel::getId)
                    .isNull();
    }
}