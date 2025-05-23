package com.example.bookcatalog.service.strategy;

import com.example.bookcatalog.model.entry.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TitleSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(String query, BookRepository repository) {
        return repository.findByTitleContaining(query);
    }
}