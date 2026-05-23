package com.mancel.yann.bookstore_api.presentation.controllers;

import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.exceptions.EntityNotFoundException;
import com.mancel.yann.bookstore_api.domain.exceptions.TransactionException;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.useCases.FindAllUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.FindByIdUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;
import com.mancel.yann.bookstore_api.presentation.mappers.ControllerMapper;
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

    private final ControllerMapper<AuthorCreationRequestDto, AuthorModel, AuthorResponseDto> authorControllerMapper;
    private final FindAllUseCase<AuthorModel> findAllUseCase;
    private final FindByIdUseCase<AuthorModel> findByIdUseCase;
    private final SaveUseCase<AuthorModel> saveUseCase;

    public AuthorController(
            ControllerMapper<AuthorCreationRequestDto, AuthorModel, AuthorResponseDto> authorControllerMapper,
            FindAllUseCase<AuthorModel> findAllUseCase,
            FindByIdUseCase<AuthorModel> findByIdUseCase,
            SaveUseCase<AuthorModel> saveUseCase) {
        this.authorControllerMapper = authorControllerMapper;
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

    @ExceptionHandler(TransactionException.class)
    public ErrorResponse resolveValidationException(TransactionException exception) {
        return ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, exception.getMessage())
                .type(URI.create("/errors/transaction-exception"))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> findAll() {
        var body = findAllUseCase
                .execute()
                .stream()
                .map(authorControllerMapper::toResponse)
                .toList();
        return ResponseEntity.ok(body);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AuthorResponseDto> findById(@PathVariable UUID id) {
        var authorModel = findByIdUseCase.execute(id);
        var body = authorControllerMapper.toResponse(authorModel);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDto> saveByRequest(@RequestBody AuthorCreationRequestDto request) {
        var transientAuthorModel = authorControllerMapper.toTransientModel(request);
        var persistedAuthorModel = saveUseCase.execute(transientAuthorModel);
        var body = authorControllerMapper.toResponse(persistedAuthorModel);
        var location = "/v1/authors/" + persistedAuthorModel.id();
        return ResponseEntity
                .created(URI.create(location))
                .body(body);
    }
}
