package com.mancel.yann.bookstore_api.data.delegates;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.exceptions.DomainException;
import com.mancel.yann.bookstore_api.domain.exceptions.UnknownException;
import com.mancel.yann.bookstore_api.domain.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;

class JpaTransactionDelegateTest {

    private final TransactionDelegate jpaTransactionDelegate = new JpaTransactionDelegate();

    @DisplayName(
            """
            Given there is a not throwing lambda
            When the executeIntoTransaction method is called
            Then the correct result is returned
            """)
    @Test
    void test1() throws DomainException {
        var uuid = Fixtures.getRandomUUID();

        var result = jpaTransactionDelegate.executeIntoTransaction(() -> uuid);

        then(result).isEqualTo(uuid);
    }

    @DisplayName(
            """
            Given there is a throwing lambda
            And the exception is not a DomainException
            When the executeIntoTransaction method is called
            Then an UnknownException is thrown
            And its cause is the lambda's exception
            """)
    @Test
    void test2() {
        var exception = new ArithmeticException();
        given(exception)
                .isInstanceOf(Exception.class)
                .isNotInstanceOf(DomainException.class);

        var thrown = catchThrowable(() -> jpaTransactionDelegate.executeIntoTransaction(() -> {
            throw exception;
        }));

        then(thrown)
                .isExactlyInstanceOf(UnknownException.class)
                .isInstanceOf(DomainException.class)
                .hasMessage(exception.getMessage())
                .hasCause(exception);
    }

    @DisplayName(
            """
            Given there is a throwing lambda
            And the exception is a DomainException
            When the executeIntoTransaction method is called
            Then this exception is rethrown
            """)
    @Test
    void test3() {
        var exception = new ValidationException("");
        given(exception).isInstanceOf(DomainException.class);

        var thrown = catchThrowable(() -> jpaTransactionDelegate.executeIntoTransaction(() -> {
            throw exception;
        }));

        then(thrown).isExactlyInstanceOf(ValidationException.class);
    }
}