package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.exceptions.EntityNotFoundException;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.impl.FindByAuthorIdUseCase;
import com.mancel.yann.bookstore_api.mocks.MockInjectorTest;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.MessageFormat;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.catchThrowable;

class FindByAuthorIdUseCaseTest extends MockInjectorTest {

    @Mock
    AuthorRepository mockedAuthorRepository;

    @InjectMocks
    FindByAuthorIdUseCase findByAuthorIdUseCase;

    @DisplayName("""
            Given the table is populated by one author
            When the execute method is called
            Then the author is returned
            """)
    @Test
    void test1() {
        var uuid = Fixtures.Author.UUID;
        BDDMockito.given(mockedAuthorRepository.findById(uuid))
                .willReturn(Optional.of(Fixtures.Author.getPersistedEntity()));

        var persistedAuthor = findByAuthorIdUseCase.execute(uuid);

        BDDMockito.then(mockedAuthorRepository)
                .should()
                .findById(uuid);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(persistedAuthor)
                .isNotNull()
                .extracting(AuthorEntity::id)
                .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given the table is empty
            When the execute method is called with random id
            Then an EntityNotFoundException is thrown
            """)
    @Test
    void test2() {
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
