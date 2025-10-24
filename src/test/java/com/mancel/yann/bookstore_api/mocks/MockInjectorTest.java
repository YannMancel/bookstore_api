package com.mancel.yann.bookstore_api.mocks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class MockInjectorTest {

    AutoCloseable closeable;

    @BeforeEach
    void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        closeable.close();
    }
}
