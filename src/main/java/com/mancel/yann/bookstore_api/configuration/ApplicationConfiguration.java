package com.mancel.yann.bookstore_api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataConfiguration.class, DomainConfiguration.class, PresentationConfiguration.class})
public class ApplicationConfiguration {
}
