package com.mancel.yann.bookstore_api.presentation.controllers;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.EntityNotFoundException;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.domain.useCases.FindAllUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.FindByIdUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/authors")
public class AuthorController {

    private final FindAllUseCase<AuthorEntity> findAllUseCase;
    private final FindByIdUseCase<AuthorEntity> findByIdUseCase;
    private final SaveUseCase<AuthorCreationRequest, AuthorEntity> saveUseCase;

    public AuthorController(FindAllUseCase<AuthorEntity> findAllUseCase,
                            FindByIdUseCase<AuthorEntity> findByIdUseCase,
                            SaveUseCase<AuthorCreationRequest, AuthorEntity> saveUseCase) {
        this.findAllUseCase = findAllUseCase;
        this.findByIdUseCase = findByIdUseCase;
        this.saveUseCase = saveUseCase;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse resolveEntityNotFoundException(EntityNotFoundException exception) {
        return ErrorResponse.builder(exception, HttpStatus.NOT_FOUND, exception.getMessage())
                .type(URI.create("/errors/entity-not-found-exception"))
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    public ErrorResponse resolveValidationException(ValidationException exception) {
        return ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, exception.getMessage())
                .type(URI.create("/errors/validation-exception"))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<AuthorEntity>> findAll() {
        return ResponseEntity.ok(findAllUseCase.execute());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AuthorEntity> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(findByIdUseCase.execute(id));
    }

    @PostMapping
    public ResponseEntity<AuthorEntity> saveByRequest(@RequestBody AuthorCreationRequest request) {
        var author = saveUseCase.execute(request);
        var location = "/v1/authors/" + author.id();
        return ResponseEntity
                .created(URI.create(location))
                .body(author);
    }
}
