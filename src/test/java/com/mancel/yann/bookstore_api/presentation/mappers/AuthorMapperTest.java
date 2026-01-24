package com.mancel.yann.bookstore_api.presentation.mappers;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

class AuthorMapperTest {

    private final Mapper<AuthorCreationRequestDto, AuthorEntity, AuthorResponseDto> mapper = new AuthorMapper();

    @DisplayName("Should convert persisted author to author response with success")
    @Test
    void test1() {
        var persistedAuthor = Fixtures.Author.getPersistedEntity();
        given(persistedAuthor)
                .isNotNull()
                .extracting(AuthorEntity::id)
                .isNotNull();

        var authorResponse = mapper.toResponse(persistedAuthor);

        then(authorResponse)
                .isNotNull()
                .extracting(AuthorResponseDto::id)
                .isNotNull();
    }

    @DisplayName("Should convert author creation response to transient author with success")
    @Test
    void test2() {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();

        var transientAuthorEntity = mapper.toTransientEntity(authorCreationRequest);

        then(transientAuthorEntity)
                .isNotNull()
                .extracting(AuthorEntity::id)
                .isNull();
    }
}