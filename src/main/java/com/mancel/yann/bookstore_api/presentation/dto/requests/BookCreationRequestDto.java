package com.mancel.yann.bookstore_api.presentation.dto.requests;

import java.util.UUID;

public record BookCreationRequestDto(String title, UUID authorId) {
}
