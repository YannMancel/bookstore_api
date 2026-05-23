package com.mancel.yann.bookstore_api.data.mappers;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.data.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

class AuthorDataMapperTest {

    @DisplayName("Should convert persisted author entity to persisted author model with success")
    @Test
    void convertAuthorEntityToAuthorModel() {
        var persistedAuthorEntity = Fixtures.Author.getPersistedEntity();
        given(persistedAuthorEntity)
                .isNotNull()
                .extracting(AuthorEntity::getId)
                    .isNotNull();

        var persistedAuthorModel = Fixtures.Author.DATA_MAPPER.toPersistedModel(persistedAuthorEntity);

        then(persistedAuthorModel)
                .isNotNull()
                .extracting(AuthorModel::id)
                    .isNotNull();
    }

    @DisplayName("Should convert transient author model to transient author entity with success")
    @Test
    void convertAuthorModelToAuthorEntity() {
        var transientAuthorModel = Fixtures.Author.getTransientModel();

        var transientAuthorEntity = Fixtures.Author.DATA_MAPPER.toTransientEntity(transientAuthorModel);

        then(transientAuthorEntity)
                .isNotNull()
                .extracting(AuthorEntity::getId)
                    .isNull();
    }
}