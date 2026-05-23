package com.mancel.yann.bookstore_api.configuration;

import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.FindAllUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.FindByIdUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.SaveUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.impl.FindAllAuthorsUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.impl.FindByAuthorIdUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.impl.SaveAuthorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public SaveUseCase<AuthorModel> createAuthorUseCase(TransactionDelegate transactionDelegate,
                                                        AuthorRepository authorRepository) {
        return new SaveAuthorUseCase(transactionDelegate, authorRepository);
    }

    @Bean
    public FindAllUseCase<AuthorModel> findAllAuthorsIdUseCase(AuthorRepository authorRepository) {
        return new FindAllAuthorsUseCase(authorRepository);
    }

    @Bean
    public FindByIdUseCase<AuthorModel> findByAuthorIdUseCase(AuthorRepository authorRepository) {
        return new FindByAuthorIdUseCase(authorRepository);
    }
}
