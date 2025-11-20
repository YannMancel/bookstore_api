package com.mancel.yann.bookstore_api.domain.useCases;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import com.mancel.yann.bookstore_api.domain.repositories.BookRepository;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;
import com.mancel.yann.bookstore_api.domain.useCases.impl.SaveBookUseCase;
import com.mancel.yann.bookstore_api.mocks.FakeTransactionDelegate;
import com.mancel.yann.bookstore_api.mocks.MockInjectorTest;
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
import org.springframework.test.context.jdbc.Sql;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings("unchecked")
class SaveBookUseCaseTest extends MockInjectorTest {

    @Spy
    TransactionDelegate fakeTransactionDelegate = new FakeTransactionDelegate();

    @Mock
    BookRepository mockedBookRepository;

    @InjectMocks
    SaveBookUseCase saveBookUseCase;

    static Stream<Arguments> invalidRequestGenerator() {
        return Stream.of(
                arguments(
                        new BookCreationRequest(null, Fixtures.Author.AUTHOR_UUID),
                        "Title is required."),
                arguments(
                        new BookCreationRequest("foo", null),
                        "Author id is required."));
    }

    @DisplayName("""
            Given there is a valid request
            And the persistence will be success
            When the execute method is called
            Then the method is executed into transaction
            And the persisted book is returned
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test1() {
        var request = Fixtures.Book.getValidBookCreationRequest();
        BDDMockito.given(mockedBookRepository.saveFromRequest(request))
                .willReturn(Fixtures.Book.getPersistedBookEntity());

        var persistedBook = saveBookUseCase.execute(request);

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .execute(any(Supplier.class));
        BDDMockito.then(mockedBookRepository)
                .should()
                .saveFromRequest(request);
        BDDMockito.then(mockedBookRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(persistedBook)
                .isEqualTo(Fixtures.Book.getPersistedBookEntity());
    }

    @DisplayName("""
            Given there is an invalid request
            When the execute method is called
            Then the method is executed into transaction
            And no persistence is performed
            And a ValidationException is thrown
            """)
    @ParameterizedTest
    @MethodSource("invalidRequestGenerator")
    void test2(BookCreationRequest request, String errorMessage) {
        var thrown = catchThrowable(() -> saveBookUseCase.execute(request));

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .execute(any(Supplier.class));
        BDDMockito.then(mockedBookRepository)
                .shouldHaveNoInteractions();
        BDDAssertions.then(thrown)
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessageContaining(errorMessage);
    }

    @DisplayName("""
            Given a valid request
            And the persistence will be fail
            When the execute method is called
            Then the method is executed into transaction
            And no persistence is performed
            And a DomainException is thrown
            """)
    @Test
    void test3() {
        var request = Fixtures.Book.getValidBookCreationRequest();
        var exception = new UnknownException("foo", new Exception("bar"));
        BDDMockito.given(mockedBookRepository.saveFromRequest(request))
                .willThrow(exception);

        var thrown = catchThrowable(() -> saveBookUseCase.execute(request));

        BDDMockito.then(fakeTransactionDelegate)
                .should()
                .execute(any(Supplier.class));
        BDDMockito.then(mockedBookRepository)
                .should()
                .saveFromRequest(request);
        BDDMockito.then(mockedBookRepository)
                .shouldHaveNoMoreInteractions();
        BDDAssertions.then(thrown)
                .isExactlyInstanceOf(UnknownException.class);
    }
}
