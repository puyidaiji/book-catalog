package com.example.bookcatalog.config;

import com.example.bookcatalog.validator.BookValidator;
import com.example.bookcatalog.validator.IsbnValidator;
import com.example.bookcatalog.validator.TitleValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationChainConfig {
    @Bean
    public BookValidator validationChain(IsbnValidator isbnValidator,
                                         TitleValidator titleValidator) {
        isbnValidator.setNext(titleValidator);
        return isbnValidator;
    }
}
