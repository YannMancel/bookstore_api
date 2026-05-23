package com.mancel.yann.bookstore_api.data.entities;

import com.mancel.yann.bookstore_api.data.entities.builders.DefaultAuthorEntityBuilder;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "authors",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            nullable = false,
            length = AuthorModel.EMAIL_LENGTH)
    private String email;

    @Column(
            name = "first_name",
            nullable = false,
            length = AuthorModel.FIRST_NAME_LENGTH)
    private String firstName;

    @Column(name =
            "last_name",
            nullable = false,
            length = AuthorModel.LAST_NAME_LENGTH)
    private String lastName;

    public AuthorEntity() {}

    public static Builder getBuilder() {
        return new DefaultAuthorEntityBuilder();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorEntity authorEntity = (AuthorEntity) o;
        return Objects.equals(id, authorEntity.id) &&
                Objects.equals(email, authorEntity.email) &&
                Objects.equals(firstName, authorEntity.firstName) &&
                Objects.equals(lastName, authorEntity.lastName);
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

        AuthorEntity build();
    }
}
