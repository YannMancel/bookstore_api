package com.mancel.yann.bookstore_api.presentation.dto.responses;

import java.util.UUID;

public record AuthorResponseDto(UUID id, String email, String firstName, String lastName) {
}
