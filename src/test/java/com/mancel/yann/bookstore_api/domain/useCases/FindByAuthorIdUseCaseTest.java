package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.exceptions.EntityNotFoundException;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.impl.FindByAuthorIdUseCase;
import com.mancel.yann.bookstore_api.mocks.MockInjector;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.MessageFormat;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.catchThrowable;

class FindByAuthorIdUseCaseTest extends MockInjector {

    @Mock
    AuthorRepository mockedAuthorRepository;

    @InjectMocks
    FindByAuthorIdUseCase findByAuthorIdUseCase;

    @DisplayName("""
            Given the table is populated by 1 author
            When the execute method is called
            Then the author is returned
            """)
    @Test
    void findPersistedAuthorModelByItsId() {
        var uuid = Fixtures.Author.UUID;
        BDDMockito.given(mockedAuthorRepository.findById(uuid))
                .willReturn(Optional.of(Fixtures.Author.getPersistedModel()));

        var persistedAuthorModel = findByAuthorIdUseCase.execute(uuid);

        BDDMockito.then(mockedAuthorRepository)
                .should()
                .findById(uuid);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(persistedAuthorModel)
                .isNotNull()
                .extracting(AuthorModel::id)
                    .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given the table is empty
            When the execute method is called with random id
            Then an EntityNotFoundException is thrown
            """)
    @Test
    void shouldThrowAnEntityNotFoundException() {
        var uuid = Fixtures.getRandomUUID();

        var thrown = catchThrowable(() -> findByAuthorIdUseCase.execute(uuid));

        BDDMockito.then(mockedAuthorRepository)
                .should()
                .findById(uuid);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(thrown)
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage(MessageFormat.format("Author is not found with {0}", uuid.toString()));
    }
}
