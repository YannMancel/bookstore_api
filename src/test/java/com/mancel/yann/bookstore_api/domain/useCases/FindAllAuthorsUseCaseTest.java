package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.impl.FindAllAuthorsUseCase;
import com.mancel.yann.bookstore_api.mocks.MockInjectorTest;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

class FindAllAuthorsUseCaseTest extends MockInjectorTest {

    @Mock
    AuthorRepository mockedAuthorRepository;

    @InjectMocks
    FindAllAuthorsUseCase findAllAuthorsUseCase;

    @DisplayName("""
            Given the table is empty
            When the execute method is called
            Then an empty list is returned
            """)
    @Test
    void test1() {
        var persistedAuthors = findAllAuthorsUseCase.execute();

        BDDMockito.then(mockedAuthorRepository)
                .should()
                .findAll();
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(persistedAuthors)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the table is populated by one author
            When the execute method is called
            Then a list is returned with this author
            """)
    @Test
    void test2() {
        BDDMockito.given(mockedAuthorRepository.findAll())
                .willReturn(List.of(Fixtures.Author.getPersistedEntity()));

        var persistedAuthors = findAllAuthorsUseCase.execute();

        BDDMockito.then(mockedAuthorRepository)
                .should()
                .findAll();
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(persistedAuthors)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                .extracting(AuthorEntity::id)
                .isEqualTo(Fixtures.Author.UUID);
    }
}
