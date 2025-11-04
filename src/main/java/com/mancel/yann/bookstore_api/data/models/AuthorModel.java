package com.mancel.yann.bookstore_api.data.models;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "authors")
public class AuthorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    public AuthorModel() {}

    public AuthorEntity getAuthorEntity() {
        return new AuthorEntity(
                getId(),
                getEmail(),
                getFirstName(),
                getLastName());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static Builder getBuilder() {
        return new DefaultAuthorModelBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorModel author = (AuthorModel) o;
        return Objects.equals(id, author.id) && Objects.equals(email, author.email) && Objects.equals(firstName, author.firstName) && Objects.equals(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName);
    }

    public interface Builder {
        Builder setId(UUID id);

        Builder setEmail(String email);

        Builder setFirstName(String firstName);

        Builder setLastName(String lastName);

        AuthorModel build();
    }
}
