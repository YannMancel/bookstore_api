package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.impl.FindAllAuthorsUseCase;
import com.mancel.yann.bookstore_api.mocks.MockInjector;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

class FindAllAuthorsUseCaseTest extends MockInjector {

    @Mock
    AuthorRepository mockedAuthorRepository;

    @InjectMocks
    FindAllAuthorsUseCase findAllAuthorsUseCase;

    @DisplayName("""
            Given the table is populated by 1 author
            When the execute method is called
            Then a list is returned with this author
            """)
    @Test
    void findAllPersistedAuthorModels() {
        BDDMockito.given(mockedAuthorRepository.findAll())
                .willReturn(List.of(Fixtures.Author.getPersistedModel()));

        var persistedAuthorModels = findAllAuthorsUseCase.execute();

        BDDMockito.then(mockedAuthorRepository)
                .should()
                .findAll();
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(persistedAuthorModels)
                .isNotNull()
                .hasSize(1)
                .allMatch(persistedAuthorModel -> persistedAuthorModel.id()
                        .equals(Fixtures.Author.UUID));
    }
}
