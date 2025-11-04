package com.mancel.yann.bookstore_api.domain.requests;

public sealed interface Request permits AuthorCreationRequest, BookCreationRequest { }
