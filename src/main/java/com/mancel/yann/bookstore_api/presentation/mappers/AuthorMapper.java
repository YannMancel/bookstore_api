package com.mancel.yann.bookstore_api.presentation.mappers;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;

public class AuthorMapper implements Mapper<AuthorCreationRequestDto, AuthorEntity, AuthorResponseDto> {
    @Override
    public AuthorResponseDto toResponse(AuthorEntity entity) {
        return new AuthorResponseDto(
                entity.id(),
                entity.email(),
                entity.firstName(),
                entity.lastName());
    }

    @Override
    public AuthorEntity toTransientEntity(AuthorCreationRequestDto request) {
        return new AuthorEntity(
                request.email(),
                request.firstName(),
                request.lastName());
    }
}
