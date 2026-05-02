package com.mancel.yann.bookstore_api.configuration;

import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import com.mancel.yann.bookstore_api.presentation.dto.requests.AuthorCreationRequestDto;
import com.mancel.yann.bookstore_api.presentation.dto.responses.AuthorResponseDto;
import com.mancel.yann.bookstore_api.presentation.mappers.AuthorControllerMapper;
import com.mancel.yann.bookstore_api.presentation.mappers.ControllerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PresentationConfiguration {

    @Bean
    public ControllerMapper<AuthorCreationRequestDto, AuthorEntity, AuthorResponseDto> authorControllerMapper() {
        return new AuthorControllerMapper();
    }
}
