package com.mancel.yann.bookstore_api.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.MvcResultTools;
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
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ControllerMapper<AuthorCreationRequestDto, AuthorModel, AuthorResponseDto> controllerMapper;

    @MockitoBean
    private FindAllUseCase<AuthorModel> findAllUseCase;

    @MockitoBean
    private FindByIdUseCase<AuthorModel> findByIdUseCase;

    @MockitoBean
    private SaveUseCase<AuthorModel> saveUseCase;

    @DisplayName("""
            Given the findAll use case returns a list containing one author
            And this author is mapped in author response
            When the findAll method is called
            Then a list is returned with the author response
            """)
    @Test
    void findAllPersistedAuthorModels() throws Exception {
        var persistedAuthorModel = Fixtures.Author.getPersistedModel();
        var persistedAuthorModels = List.of(persistedAuthorModel);
        var authorResponses = persistedAuthorModels
                .stream()
                .map(Fixtures.Author.CONTROLLER_MAPPER::toResponse)
                .toList();
        given(findAllUseCase.execute())
                .willReturn(persistedAuthorModels);
        given(controllerMapper.toResponse(persistedAuthorModel))
                .willReturn(Fixtures.Author.CONTROLLER_MAPPER.toResponse(persistedAuthorModel));

        var request = get("/v1/authors")
                .contentType(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(request)
                .andDo(print())
                .andReturn();

        BDDMockito.then(findAllUseCase)
                .should()
                .execute();
        BDDAssertions.then(result)
                .isNotNull()
                .matches(mvcResult -> MvcResultTools.isMethodName(mvcResult, "findAll"),
                        "is correct method")
                .matches(mvcResult -> MvcResultTools.isStatus(mvcResult, HttpStatus.OK),
                        "is Ok")
                .matches(mvcResult -> MvcResultTools.hasContent(mvcResult, objectMapper, authorResponses),
                        "has correct author responses");
    }

    @DisplayName("""
            Given the findById use case returns a author by its id
            And this author is mapped in author response
            When the findById method is called
            Then the author response is returned
            """)
    @Test
    void findPersistedAuthorModelByItsId() throws Exception {
        var uuid = Fixtures.Author.UUID;
        var persistedAuthorModel = Fixtures.Author.getPersistedModel();
        var response = Fixtures.Author.CONTROLLER_MAPPER.toResponse(persistedAuthorModel);
        given(findByIdUseCase.execute(uuid))
                .willReturn(persistedAuthorModel);
        given(controllerMapper.toResponse(persistedAuthorModel))
                .willReturn(response);

        var request = get("/v1/authors/{id}", uuid)
                .contentType(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(request)
                .andDo(print())
                .andReturn();

        BDDMockito.then(findByIdUseCase)
                .should()
                .execute(uuid);
        BDDAssertions.then(result)
                .isNotNull()
                .matches(mvcResult -> MvcResultTools.isMethodName(mvcResult, "findById"),
                        "is correct method")
                .matches(mvcResult -> MvcResultTools.isStatus(mvcResult, HttpStatus.OK),
                        "is Ok")
                .matches(mvcResult -> MvcResultTools.hasContent(mvcResult, objectMapper, response),
                        "has correct author response");
    }

    @DisplayName("""
            Given the findById use case throws an EntityNotFoundException
            When the findById method is called
            Then the response is a 404 Not Found
            """)
    @Test
    void shouldBeA404NotFound() throws Exception {
        var uuid = Fixtures.getRandomUUID();
        var exception = new EntityNotFoundException("foo");
        given(findByIdUseCase.execute(uuid))
                .willThrow(exception);

        var request = get("/v1/authors/{id}", uuid)
                .contentType(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(request)
                .andDo(print())
                .andReturn();

        BDDMockito.then(findByIdUseCase)
                .should()
                .execute(uuid);
        BDDAssertions.then(result)
                .isNotNull()
                .matches(mvcResult -> MvcResultTools.isMethodName(mvcResult, "findById"),
                        "is correct method")
                .matches(mvcResult -> MvcResultTools.isStatus(mvcResult, HttpStatus.NOT_FOUND),
                        "is Not Found")
                .matches(mvcResult -> MvcResultTools.hasException(mvcResult, exception),
                        "has correct exception");
    }

    @DisplayName("""
            Given this author creation request is mapped in transient author
            And the save use case returns an author
            And this author is mapped in author response
            When the saveByRequest method is called
            Then the author response is returned
            """)
    @Test
    void persistTransientAuthorModel() throws Exception {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();
        var transientAuthorModel = Fixtures.Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);
        var persistedAuthorModel = Fixtures.Author.getPersistedModel();
        var authorResponse = Fixtures.Author.CONTROLLER_MAPPER.toResponse(persistedAuthorModel);
        given(controllerMapper.toTransientModel(authorCreationRequest))
                .willReturn(transientAuthorModel);
        given(saveUseCase.execute(transientAuthorModel))
                .willReturn(persistedAuthorModel);
        given(controllerMapper.toResponse(persistedAuthorModel))
                .willReturn(authorResponse);

        var request = post("/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorCreationRequest));
        var result = mockMvc.perform(request)
                .andDo(print())
                .andReturn();

        BDDMockito.then(saveUseCase)
                .should()
                .execute(transientAuthorModel);
        BDDAssertions.then(result)
                .isNotNull()
                .matches(mvcResult -> MvcResultTools.isMethodName(mvcResult, "saveByRequest"),
                        "is correct method")
                .matches(mvcResult -> MvcResultTools.isStatus(mvcResult, HttpStatus.CREATED),
                        "is CREATED")
                .matches(mvcResult -> MvcResultTools.hasContent(mvcResult, objectMapper, authorResponse),
                        "has correct author response");
    }

    @DisplayName("""
            Given this author creation request is mapped in transient author
            And the save use case throws a ValidationException
            When the saveByRequest method is called
            Then the response is a 400 Bad Request
            """)
    @Test
    void shouldBeA400BadRequestForDomainException() throws Exception {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();
        var transientAuthorModel = Fixtures.Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);
        var exception = new ValidationException("foo");
        given(controllerMapper.toTransientModel(authorCreationRequest))
                .willReturn(transientAuthorModel);
        given(saveUseCase.execute(transientAuthorModel))
                .willThrow(exception);

        var request = post("/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorCreationRequest));
        var result = mockMvc.perform(request)
                .andDo(print())
                .andReturn();

        BDDMockito.then(saveUseCase)
                .should()
                .execute(transientAuthorModel);
        BDDAssertions.then(result)
                .isNotNull()
                .matches(mvcResult -> MvcResultTools.isMethodName(mvcResult, "saveByRequest"),
                        "is correct method")
                .matches(mvcResult -> MvcResultTools.isStatus(mvcResult, HttpStatus.BAD_REQUEST),
                        "is Bad Request")
                .matches(mvcResult -> MvcResultTools.hasException(mvcResult, exception),
                        "has correct exception");
    }

    @DisplayName("""
            Given this author creation request is mapped in transient author
            And the save use case throws a TransactionException
            When the saveByRequest method is called
            Then the response is a 400 Bad Request
            """)
    @Test
    void shouldBeA400BadRequestFromTransactionException() throws Exception {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();
        var transientAuthorModel = Fixtures.Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);
        var exception = new TransactionException("foo", new Exception());
        given(controllerMapper.toTransientModel(authorCreationRequest))
                .willReturn(transientAuthorModel);
        given(saveUseCase.execute(transientAuthorModel))
                .willThrow(exception);

        var request = post("/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorCreationRequest));
        var result = mockMvc.perform(request)
                .andDo(print())
                .andReturn();

        BDDMockito.then(saveUseCase)
                .should()
                .execute(transientAuthorModel);
        BDDAssertions.then(result)
                .isNotNull()
                .matches(mvcResult -> MvcResultTools.isMethodName(mvcResult, "saveByRequest"),
                        "is correct method")
                .matches(mvcResult -> MvcResultTools.isStatus(mvcResult, HttpStatus.BAD_REQUEST),
                        "is Bad Request")
                .matches(mvcResult -> MvcResultTools.hasException(mvcResult, exception),
                        "has correct exception");
    }
}
