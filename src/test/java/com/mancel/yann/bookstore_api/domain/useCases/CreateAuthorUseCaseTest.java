package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.mocks.MockInjectorTest;
import com.mancel.yann.bookstore_api.domain.delegates.ThrowableSupplier;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.mocks.FakeTransactionDelegate;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;

import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings("unchecked")
class CreateAuthorUseCaseTest extends MockInjectorTest {

    @Mock
    AuthorRepository mockedAuthorRepository;

    @Spy
    TransactionDelegate fakeTransactionDelegate = new FakeTransactionDelegate();

    @InjectMocks
    CreateAuthorUseCase createAuthorUseCase;

    @DisplayName(
            """
            Given there is a valid author creation request
            And the persistence will be success
            When the execute method is called
            Then the method is executed into transaction
            And the persisted author is returned
            """)
    @Test
    void test1() throws ValidationException {
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
        BDDAssertions.then(persistedAuthor)
                .isEqualTo(Fixtures.getPersistedAuthor());
    }

    static Stream<Arguments> invalidAuthorCreationRequestGenerator() {
        return Stream.of(
                arguments(
                        Fixtures.getInvalidAuthorCreationRequest(true, false),
                        "First name is required."),
                arguments(Fixtures.getInvalidAuthorCreationRequest(false, true),
                        "Last name is required."),
                arguments(Fixtures.getInvalidAuthorCreationRequest(true, true),
                        "First name is required.Last name is required.")
        );
    }

    @DisplayName(
            """
            Given there is an invalid author creation request
            When the execute method is called
            Then the method is executed into transaction
            And no persistence is performed
            And a ValidationException is thrown
            """)
    @ParameterizedTest
    @MethodSource("invalidAuthorCreationRequestGenerator")
    void test2(
            AuthorCreationRequest  authorCreationRequest,
            String errorMessage) {

        var thrown = catchThrowable(() -> createAuthorUseCase.execute(authorCreationRequest));

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .executeIntoTransaction(any(ThrowableSupplier.class));
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoInteractions();
        BDDAssertions.then(thrown)
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessageContaining(errorMessage);
    }

    @DisplayName(
            """
            Given a valid author creation request
            And the persistence will be fail
            When the execute method is called
            Then the method is executed into transaction
            And null is returned
            """)
    @Test
    void test3() throws ValidationException {
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
