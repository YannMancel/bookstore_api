package com.mancel.yann.bookstore_api.configuration;

import com.mancel.yann.bookstore_api.domain.repositories.AuthorRepository;
import com.mancel.yann.bookstore_api.domain.useCases.CreateAuthorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.mancel.yann.bookstore_api.data.repositories")
public class ApplicationConfiguration {

    @Bean
    public CreateAuthorUseCase createAuthorUseCase(AuthorRepository authorRepository) {
        return new CreateAuthorUseCase(authorRepository);
    }
}
