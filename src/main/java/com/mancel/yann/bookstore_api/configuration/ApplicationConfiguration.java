package com.mancel.yann.bookstore_api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.mancel.yann.bookstore_api.data.repositories")
public class ApplicationConfiguration {
}
