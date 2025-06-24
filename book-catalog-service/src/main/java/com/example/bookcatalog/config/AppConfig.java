package com.example.bookcatalog.config;

import com.example.bookcatalog.model.entry.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return args -> {
            // 初始化测试数据
            repository.save(Book.builder()
                    .title("Test Title1")
                    .author("auth1")
                    .isbn("XXXX111111111")
                    .publicationYear(2008)
                    .genre("genre1")
                    .build());

            repository.save(Book.builder()
                    .title("Test Title2")
                    .author("auth2")
                    .isbn("XXXX222222222")
                    .publicationYear(2018)
                    .genre("genre2")
                    .build());
        };
    }
}
