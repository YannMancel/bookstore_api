package com.mancel.yann.bookstore_api.presentation.mappers;

import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;
import org.springframework.util.Assert;

public class AuthorControllerMapper implements ControllerMapper<AuthorCreationRequestDto, AuthorModel, AuthorResponseDto> {
    @Override
    public AuthorResponseDto toResponse(AuthorModel persistedModel) {
        Assert.notNull(persistedModel.id(), "Persisted author model's id must be not null.");
        return new AuthorResponseDto(
                persistedModel.id(),
                persistedModel.email(),
                persistedModel.firstName(),
                persistedModel.lastName());
    }

    @Override
    public AuthorModel toTransientModel(AuthorCreationRequestDto request) {
        return new AuthorModel(
                request.email(),
                request.firstName(),
                request.lastName());
    }
}
