package com.mancel.yann.bookstore_api.data.models;

import com.mancel.yann.bookstore_api.data.models.builders.DefaultBookModelBuilder;
import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "books")
public class BookModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    private AuthorModel author;

    public BookModel() {
    }

    public static Builder getBuilder() {
        return new DefaultBookModelBuilder();
    }

    public BookEntity getBookEntity() {
        return new BookEntity(getId(), getTitle(), getAuthor().getAuthorEntity());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorModel getAuthor() {
        return author;
    }

    public void setAuthor(AuthorModel author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookModel book = (BookModel) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author);
    }

    public interface Builder {
        Builder setId(UUID id);

        Builder setTitle(String title);

        Builder setAuthor(AuthorModel author);

        BookModel build();
    }
}
