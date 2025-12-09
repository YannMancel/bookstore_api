package com.mancel.yann.bookstore_api.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.MvcResultTools;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.EntityNotFoundException;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.domain.useCases.FindAllUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.FindByIdUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;
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
    private FindAllUseCase<AuthorEntity> findAllUseCase;

    @MockitoBean
    private FindByIdUseCase<AuthorEntity> findByIdUseCase;

    @MockitoBean
    private SaveUseCase<AuthorCreationRequest, AuthorEntity> saveUseCase;

    @DisplayName("""
            Given the findAll use case returns a list containing one author
            When the findAll method is called
            Then a list is returned with this author
            """)
    @Test
    void test1() throws Exception {
        var authors = List.of(Fixtures.Author.getPersistedAuthorEntity());
        given(findAllUseCase.execute())
                .willReturn(authors);

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
                .matches(mvcResult -> MvcResultTools.hasContent(mvcResult, objectMapper, authors),
                        "has correct authors");
    }

    @DisplayName("""
            Given the findById use case returns a author by its id
            When the findById method is called
            Then this author is returned
            """)
    @Test
    void test2() throws Exception {
        var uuid = Fixtures.Author.AUTHOR_UUID;
        var author = Fixtures.Author.getPersistedAuthorEntity();
        given(findByIdUseCase.execute(uuid))
                .willReturn(author);

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
                .matches(mvcResult -> MvcResultTools.hasContent(mvcResult, objectMapper, author),
                        "has correct author");
    }

    @DisplayName("""
            Given the findById use case throws a NoEntityFoundException
            When the findById method is called
            Then the response is a 404 Not Found
            """)
    @Test
    void test3() throws Exception {
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
            Given the save use case returns an author
            When the saveByRequest method is called
            Then this author is returned
            """)
    @Test
    void test4() throws Exception {
        var authorCreationRequest = Fixtures.Author.getValidAuthorCreationRequest();
        var author = Fixtures.Author.getPersistedAuthorEntity();
        given(saveUseCase.execute(authorCreationRequest))
                .willReturn(author);

        var request = post("/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorCreationRequest));
        var result = mockMvc.perform(request)
                .andDo(print())
                .andReturn();

        BDDMockito.then(saveUseCase)
                .should()
                .execute(authorCreationRequest);
        BDDAssertions.then(result)
                .isNotNull()
                .matches(mvcResult -> MvcResultTools.isMethodName(mvcResult, "saveByRequest"),
                        "is correct method")
                .matches(mvcResult -> MvcResultTools.isStatus(mvcResult, HttpStatus.CREATED),
                        "is CREATED")
                .matches(mvcResult -> MvcResultTools.hasContent(mvcResult, objectMapper, author),
                        "has correct author");
    }

    @DisplayName("""
            Given the save use case throws a ValidationException
            When the saveByRequest method is called
            Then the response is a 400 Bad Request
            """)
    @Test
    void test5() throws Exception {
        var authorCreationRequest = Fixtures.Author.getValidAuthorCreationRequest();
        var exception = new ValidationException("foo");
        given(saveUseCase.execute(authorCreationRequest))
                .willThrow(exception);

        var request = post("/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorCreationRequest));
        var result = mockMvc.perform(request)
                .andDo(print())
                .andReturn();

        BDDMockito.then(saveUseCase)
                .should()
                .execute(authorCreationRequest);
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
