package com.mancel.yann.bookstore_api.presentation.controllers;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.domain.useCases.FindAllUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.FindByIdUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<AuthorEntity> findAll() {
        return findAllUseCase.execute();
    }

    @GetMapping(path = "/{id}")
    public AuthorEntity findById(@PathVariable UUID id) {
        return findByIdUseCase.execute(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AuthorEntity saveByRequest(@RequestBody AuthorCreationRequest request) {
        return saveUseCase.execute(request);
    }
}
