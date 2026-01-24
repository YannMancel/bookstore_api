package com.mancel.yann.bookstore_api.configuration;

import com.mancel.yann.bookstore_api.data.delegates.JpaTransactionDelegate;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.impl.SaveAuthorUseCase;
import com.mancel.yann.bookstore_api.presentation.mappers.AuthorMapper;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.domain.useCases.*;
import com.mancel.yann.bookstore_api.domain.useCases.impl.FindAllAuthorsUseCase;
import com.mancel.yann.bookstore_api.domain.useCases.impl.FindByAuthorIdUseCase;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;
import com.mancel.yann.bookstore_api.presentation.mappers.Mapper;
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
    public Mapper<AuthorCreationRequestDto, AuthorEntity, AuthorResponseDto> authorMapper() {
        return new AuthorMapper();
    }

    @Bean
    public SaveUseCase<AuthorEntity> createAuthorUseCase(TransactionDelegate transactionDelegate,
                                                         AuthorRepository authorRepository) {
        return new SaveAuthorUseCase(transactionDelegate, authorRepository);
    }

    @Bean
    public FindAllUseCase<AuthorEntity> findAllAuthorsIdUseCase(AuthorRepository authorRepository) {
        return new FindAllAuthorsUseCase(authorRepository);
    }

    @Bean
    public FindByIdUseCase<AuthorEntity> findByAuthorIdUseCase(AuthorRepository authorRepository) {
        return new FindByAuthorIdUseCase(authorRepository);
    }
}
