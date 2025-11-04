package com.mancel.yann.bookstore_api.domain.requests;

import java.util.UUID;

public record BookCreationRequest(String title, UUID authorId) implements Request { }
