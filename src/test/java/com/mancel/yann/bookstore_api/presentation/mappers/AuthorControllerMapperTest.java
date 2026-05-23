package com.mancel.yann.bookstore_api.presentation.mappers;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

class AuthorControllerMapperTest {

    @DisplayName("Should convert persisted author to author response with success")
    @Test
    void convertAuthorModelToAuthorResponse() {
        var persistedAuthorModel = Fixtures.Author.getPersistedModel();
        given(persistedAuthorModel)
                .isNotNull()
                .extracting(AuthorModel::id)
                    .isNotNull();

        var authorResponse = Fixtures.Author.CONTROLLER_MAPPER.toResponse(persistedAuthorModel);

        then(authorResponse)
                .isNotNull()
                .extracting(AuthorResponseDto::id)
                    .isNotNull();
    }

    @DisplayName("Should convert author creation response to transient author with success")
    @Test
    void convertAuthorCreationRequestToTransientAuthorModel() {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();

        var transientAuthorModel = Fixtures.Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);

        then(transientAuthorModel)
                .isNotNull()
                .extracting(AuthorModel::id)
                    .isNull();
    }
}