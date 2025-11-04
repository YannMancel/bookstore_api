package com.mancel.yann.bookstore_api.data.models.builders;

import com.mancel.yann.bookstore_api.data.models.AuthorModel;
import com.mancel.yann.bookstore_api.data.models.BookModel;

import java.util.UUID;

public class DefaultBookModelBuilder implements BookModel.Builder {

    final BookModel bookModel;

    public DefaultBookModelBuilder() {
        this.bookModel = new BookModel();
    }

    @Override
    public BookModel.Builder setId(UUID id) {
        bookModel.setId(id);
        return this;
    }

    @Override
    public BookModel.Builder setTitle(String title) {
        bookModel.setTitle(title);
        return this;
    }

    @Override
    public BookModel.Builder setAuthor(AuthorModel author) {
        bookModel.setAuthor(author);
        return this;
    }

    @Override
    public BookModel build() {
        return bookModel;
    }
}
