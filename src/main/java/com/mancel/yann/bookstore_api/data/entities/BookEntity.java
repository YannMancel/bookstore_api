package com.mancel.yann.bookstore_api.data.entities;

import com.mancel.yann.bookstore_api.data.entities.builders.DefaultBookEntityBuilder;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "books",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title"}))
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "authors_books",
            joinColumns= @JoinColumn(name="book_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="author_id", referencedColumnName="id"))
    private Set<AuthorEntity> authors = new HashSet<>();

    public BookEntity() {}

    public static Builder getBuilder() {
        return new DefaultBookEntityBuilder();
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

    public Set<AuthorEntity> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorEntity> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity bookEntity = (BookEntity) o;
        return Objects.equals(id, bookEntity.id) &&
                Objects.equals(title, bookEntity.title) &&
                Objects.equals(authors, bookEntity.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authors);
    }

    public interface Builder {
        Builder setId(UUID id);

        Builder setTitle(String title);

        Builder setAuthors(Set<AuthorEntity> authors);

        BookEntity build();
    }
}
