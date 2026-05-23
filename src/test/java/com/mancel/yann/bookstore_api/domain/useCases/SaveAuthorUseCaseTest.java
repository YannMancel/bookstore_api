package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.impl.SaveAuthorUseCase;
import com.mancel.yann.bookstore_api.mocks.FakeTransactionDelegate;
import com.mancel.yann.bookstore_api.mocks.MockInjector;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings("unchecked")
class SaveAuthorUseCaseTest extends MockInjector {

    @Spy
    TransactionDelegate fakeTransactionDelegate = new FakeTransactionDelegate();

    @Mock
    AuthorRepository mockedAuthorRepository;

    @InjectMocks
    SaveAuthorUseCase saveAuthorUseCase;

    static Stream<Arguments> invalidTransientAuthorModelGenerator() {
        return Stream.of(
                arguments(
                        new AuthorModel("john.doe@gmail.com", null, "Doe"),
                        "First name is required."),
                arguments(
                        new AuthorModel("john.doe@gmail.com", "John", null),
                        "Last name is required."));
    }

    @DisplayName("""
            Given there is a valid transient author
            And the persistence will be success
            When the execute method is called
            Then the method is executed into transaction
            And the persisted author is returned
            """)
    @Test
    void persistTransientAuthorModel() {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();
        var transientAuthorModel = Fixtures.Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);
        BDDMockito.given(mockedAuthorRepository.save(transientAuthorModel))
                .willReturn(Fixtures.Author.getPersistedModel());

        var persistedAuthorModel = saveAuthorUseCase.execute(transientAuthorModel);

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .execute(any(Supplier.class));
        BDDMockito.then(mockedAuthorRepository)
                .should()
                .save(transientAuthorModel);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(persistedAuthorModel)
                .isEqualTo(Fixtures.Author.getPersistedModel());
    }

    @DisplayName("""
            Given there is an invalid transient author
            When the execute method is called
            Then the method is executed into transaction
            And no persistence is performed
            And a ValidationException is thrown
            """)
    @ParameterizedTest
    @MethodSource("invalidTransientAuthorModelGenerator")
    void shouldThrowAnExceptionForANullableField(
            AuthorModel transientAuthorModel,
            String errorMessage
    ) {
        var thrown = catchThrowable(() -> saveAuthorUseCase.execute(transientAuthorModel));

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .execute(any(Supplier.class));
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoInteractions();
        BDDAssertions.then(thrown)
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessageContaining(errorMessage);
    }

    @DisplayName("""
            Given a valid transient author
            And the persistence will be fail
            When the execute method is called
            Then the method is executed into transaction
            And no persistence is performed
            And a DomainException is thrown
            """)
    @Test
    void shouldThrowAnUnknownException() {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();
        var transientAuthorModel = Fixtures.Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);
        var exception = new UnknownException("foo", new Exception("bar"));
        BDDMockito.given(mockedAuthorRepository.save(transientAuthorModel))
                .willThrow(exception);

        var thrown = catchThrowable(() -> saveAuthorUseCase.execute(transientAuthorModel));

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .execute(any(Supplier.class));
        BDDMockito.then(mockedAuthorRepository)
                .should()
                .save(transientAuthorModel);
        BDDMockito.then(mockedAuthorRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(thrown)
                .isExactlyInstanceOf(UnknownException.class);
    }
}
