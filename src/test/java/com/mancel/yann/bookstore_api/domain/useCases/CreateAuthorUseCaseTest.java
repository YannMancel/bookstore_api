package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.MockInjectorTestBase;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.BDDAssertions.catchThrowable;

class CreateAuthorUseCaseTest extends MockInjectorTestBase {

    @Mock
    private AuthorRepository mockedAuthorRepository;

    @InjectMocks
    private CreateAuthorUseCase createAuthorUseCase;

    @DisplayName(
            """
            Given a valid author creation request
            And the persistence will be success
            When execute method is called
            Then the persisted author is returned
            """)
    @Test
    void givenAValidAuthorCreationRequest_whenExecuteIsCalled_thenPersistenceIsSuccess() throws ValidationException {
        var authorCreationRequest = Fixtures.getValidAuthorCreationRequest();
        var transientAuthor = authorCreationRequest.convertToAuthor();
        BDDMockito.given(mockedAuthorRepository.save(transientAuthor))
                .willReturn(Fixtures.getPersistedAuthor());

        var persistedAuthor = createAuthorUseCase.execute(authorCreationRequest);

        BDDAssertions.then(persistedAuthor).isEqualTo(Fixtures.getPersistedAuthor());
        BDDMockito.then(mockedAuthorRepository)
                .should()
                .save(transientAuthor);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
    }

    @DisplayName(
            """
            Given an invalid author creation request
            When execute method is called
            Then ValidationException is thrown
            """)
    @Test
    void givenAnInvalidAuthorCreationRequest_whenExecuteIsCalled_thenValidationExceptionIsThrown() {
        var authorCreationRequest = Fixtures.getInvalidAuthorCreationRequest();
        var transientAuthor = authorCreationRequest.convertToAuthor();
        BDDMockito.given(mockedAuthorRepository.save(transientAuthor))
                .willReturn(Fixtures.getPersistedAuthor());

        var thrown = catchThrowable(() -> createAuthorUseCase.execute(authorCreationRequest));

        BDDAssertions.then(thrown)
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessageContaining("The request's firstName is null.");
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoInteractions();
    }

    @DisplayName(
            """
            Given a valid author creation request
            And the persistence will be fail
            When execute method is called
            Then null is returned
            """)
    @Test
    void givenAValidAuthorCreationRequest_whenExecuteIsCalled_thenNullIsReturned() throws ValidationException {
        var authorCreationRequest = Fixtures.getValidAuthorCreationRequest();
        var transientAuthor = authorCreationRequest.convertToAuthor();
        BDDMockito.given(mockedAuthorRepository.save(transientAuthor))
                .willReturn(null);

        var unpersistedAuthor = createAuthorUseCase.execute(authorCreationRequest);

        BDDAssertions.then(unpersistedAuthor)
                .isNull();
        BDDMockito.then(mockedAuthorRepository)
                .should()
                .save(transientAuthor);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
    }
}
