package com.mancel.yann.bookstore_api.presentation.mappers;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;
import org.springframework.util.Assert;

public class AuthorControllerMapper implements ControllerMapper<AuthorCreationRequestDto, AuthorEntity, AuthorResponseDto> {
    @Override
    public AuthorResponseDto toResponse(AuthorEntity persistedEntity) {
        Assert.notNull(persistedEntity.id(), "Persisted author's id must be not null.");
        return new AuthorResponseDto(
                persistedEntity.id(),
                persistedEntity.email(),
                persistedEntity.firstName(),
                persistedEntity.lastName());
    }

    @Override
    public AuthorEntity toTransientEntity(AuthorCreationRequestDto request) {
        return new AuthorEntity(
                request.email(),
                request.firstName(),
                request.lastName());
    }
}
