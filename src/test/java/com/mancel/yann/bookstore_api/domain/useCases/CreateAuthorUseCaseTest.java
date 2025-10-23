package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.MockInjectorTestBase;
import com.mancel.yann.bookstore_api.domain.delegates.ThrowableSupplier;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.mocks.FakeTransactionDelegate;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings("unchecked")
class CreateAuthorUseCaseTest extends MockInjectorTestBase {

    @Mock
    private AuthorRepository mockedAuthorRepository;

    @Spy
    private TransactionDelegate fakeTransactionDelegate = new FakeTransactionDelegate();

    @InjectMocks
    private CreateAuthorUseCase createAuthorUseCase;

    @DisplayName(
            """
            Given there is a valid author creation request
            And the persistence will be success
            When execute method is called
            Then the method is executed into transaction
            And the persisted author is returned
            """)
    @Test
    void givenAValidAuthorCreationRequest_whenExecuteIsCalled_thenPersistenceIsSuccess() throws ValidationException {
        var authorCreationRequest = Fixtures.getValidAuthorCreationRequest();
        var transientAuthor = authorCreationRequest.convertToAuthor();
        BDDMockito.given(mockedAuthorRepository.save(transientAuthor))
                .willReturn(Fixtures.getPersistedAuthor());

        var persistedAuthor = createAuthorUseCase.execute(authorCreationRequest);

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .executeIntoTransaction(any(ThrowableSupplier.class));
        BDDMockito.then(mockedAuthorRepository)
                .should()
                .save(transientAuthor);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(persistedAuthor).isEqualTo(Fixtures.getPersistedAuthor());
    }

    @DisplayName(
            """
            Given there is an invalid author creation request
            When execute method is called
            Then the method is executed into transaction
            And no persistence is performed
            And a ValidationException is thrown
            """)
    @Test
    void givenAnInvalidAuthorCreationRequest_whenExecuteIsCalled_thenValidationExceptionIsThrown() {
        var authorCreationRequest = Fixtures.getInvalidAuthorCreationRequest();

        var thrown = catchThrowable(() -> createAuthorUseCase.execute(authorCreationRequest));

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .executeIntoTransaction(any(ThrowableSupplier.class));
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoInteractions();
        BDDAssertions.then(thrown)
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessageContaining("The request's firstName is null.");
    }

    @DisplayName(
            """
            Given a valid author creation request
            And the persistence will be fail
            When execute method is called
            Then the method is executed into transaction
            And null is returned
            """)
    @Test
    void givenAValidAuthorCreationRequest_whenExecuteIsCalled_thenNullIsReturned() throws ValidationException {
        var authorCreationRequest = Fixtures.getValidAuthorCreationRequest();
        var transientAuthor = authorCreationRequest.convertToAuthor();
        BDDMockito.given(mockedAuthorRepository.save(transientAuthor))
                .willReturn(null);

        var unpersistedAuthor = createAuthorUseCase.execute(authorCreationRequest);

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .executeIntoTransaction(any(ThrowableSupplier.class));
        BDDMockito.then(mockedAuthorRepository)
                .should()
                .save(transientAuthor);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(unpersistedAuthor)
                .isNull();
    }
}
