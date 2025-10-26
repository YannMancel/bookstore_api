package com.mancel.yann.bookstore_api.domain.requests;

public record AuthorCreationRequest(String email, String firstName, String lastName) implements Request { }
