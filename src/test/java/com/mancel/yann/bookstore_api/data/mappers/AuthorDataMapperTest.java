package com.mancel.yann.bookstore_api.data.mappers;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

class AuthorDataMapperTest {
    private final DataMapper<AuthorEntity, AuthorModel> mapper = new AuthorDataMapper();

    @DisplayName("Should convert persisted author model to persisted author entity with success")
    @Test
    void test1() {
        var transientAuthorModel = Fixtures.Author.getTransientModel();
        var persistedAuthorModel = AuthorModel.getBuilder()
                .setId(Fixtures.Author.UUID)
                .setEmail(transientAuthorModel.getEmail())
                .setFirstName(transientAuthorModel.getFirstName())
                .setLastName(transientAuthorModel.getLastName())
                .build();
        given(persistedAuthorModel)
                .isNotNull()
                .extracting(AuthorModel::getId)
                .isNotNull();

        var persistedAuthorEntity = mapper.toPersistedEntity(persistedAuthorModel);

        then(persistedAuthorEntity)
                .isNotNull()
                .extracting(AuthorEntity::id)
                .isNotNull();
    }

    @DisplayName("Should convert transient author entity to transient author model with success")
    @Test
    void test2() {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();
        var transientAuthorEntity = new AuthorEntity(
                authorCreationRequest.email(),
                authorCreationRequest.firstName(),
                authorCreationRequest.lastName());

        var transientAuthorModel = mapper.toTransientModel(transientAuthorEntity);

        then(transientAuthorModel)
                .isNotNull()
                .extracting(AuthorModel::getId)
                .isNull();
    }
}