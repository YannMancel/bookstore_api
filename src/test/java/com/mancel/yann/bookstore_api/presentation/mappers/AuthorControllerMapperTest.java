package com.mancel.yann.bookstore_api.presentation.mappers;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

class AuthorControllerMapperTest {

    @DisplayName("Should convert persisted author to author response with success")
    @Test
    void test1() {
        var persistedEntity = Fixtures.Author.getPersistedEntity();
        given(persistedEntity)
                .isNotNull()
                .extracting(AuthorEntity::id)
                    .isNotNull();

        var response = Fixtures.Author.CONTROLLER_MAPPER.toResponse(persistedEntity);

        then(response)
                .isNotNull()
                .extracting(AuthorResponseDto::id)
                    .isNotNull();
    }

    @DisplayName("Should convert author creation response to transient author with success")
    @Test
    void test2() {
        var request = Fixtures.Author.getValidCreationRequest();

        var transientEntity = Fixtures.Author.CONTROLLER_MAPPER.toTransientEntity(request);

        then(transientEntity)
                .isNotNull()
                .extracting(AuthorEntity::id)
                    .isNull();
    }
}