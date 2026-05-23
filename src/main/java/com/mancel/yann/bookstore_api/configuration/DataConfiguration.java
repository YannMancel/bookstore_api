package com.mancel.yann.bookstore_api.configuration;

import com.mancel.yann.bookstore_api.data.delegates.JpaTransactionDelegate;
import com.mancel.yann.bookstore_api.data.mappers.AuthorDataMapper;
import com.mancel.yann.bookstore_api.data.mappers.DataMapper;
import com.mancel.yann.bookstore_api.data.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.domain.delegates.TransactionDelegate;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.mancel.yann.bookstore_api.data.repositories")
@EntityScan("com.mancel.yann.bookstore_api.data.entities")
public class DataConfiguration {

    @Bean
    public TransactionDelegate transactionDelegate() {
        return new JpaTransactionDelegate();
    }

    @Bean
    public DataMapper<AuthorModel, AuthorEntity> authorDataMapper() {
        return new AuthorDataMapper();
    }
}
