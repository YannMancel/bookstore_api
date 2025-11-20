package com.mancel.yann.bookstore_api.configuration;

import com.mancel.yann.bookstore_api.data.delegates.JpaTransactionDelegate;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.entities.BookEntity;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.repositories.BookRepository;
import com.mancel.yann.bookstore_api.domain.requests.AuthorCreationRequest;
import com.mancel.yann.bookstore_api.domain.requests.BookCreationRequest;
import com.mancel.yann.bookstore_api.domain.useCases.*;
import com.mancel.yann.bookstore_api.domain.useCases.impl.SaveAuthorUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.impl.SaveBookUseCase;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.mancel.yann.bookstore_api.data.repositories")
@EntityScan("com.mancel.yann.bookstore_api.data.models")
//@EnableTransactionManagement
public class ApplicationConfiguration {

    @Bean
    public TransactionDelegate transactionDelegate() {
        return new JpaTransactionDelegate();
    }

    @Bean
    public SaveUseCase<AuthorCreationRequest, AuthorEntity> createAuthorUseCase(TransactionDelegate transactionDelegate, AuthorRepository authorRepository) {
        return new SaveAuthorUseCase(transactionDelegate, authorRepository);
    }

    @Bean
    public SaveUseCase<BookCreationRequest, BookEntity> createBookUseCase(TransactionDelegate transactionDelegate, BookRepository bookRepository) {
        return new SaveBookUseCase(transactionDelegate, bookRepository);
    }
}
