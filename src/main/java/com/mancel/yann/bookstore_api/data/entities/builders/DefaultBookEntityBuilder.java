package com.mancel.yann.bookstore_api.data.entities.builders;

import com.mancel.yann.bookstore_api.data.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.data.entities.BookEntity;

import java.util.Set;
import java.util.UUID;

public class DefaultBookEntityBuilder implements BookEntity.Builder {

    final BookEntity bookEntity;

    public DefaultBookEntityBuilder() {
        this.bookEntity = new BookEntity();
    }

    @Override
    public BookEntity.Builder setId(UUID id) {
        bookEntity.setId(id);
        return this;
    }

    @Override
    public BookEntity.Builder setTitle(String title) {
        bookEntity.setTitle(title);
        return this;
    }

    @Override
    public BookEntity.Builder setAuthors(Set<AuthorEntity> authors) {
        bookEntity.setAuthors(authors);
        return this;
    }

    @Override
    public BookEntity build() {
        return bookEntity;
    }
}
