package com.example.bookcatalog.service.strategy;

import com.example.bookcatalog.model.entry.Book;
import com.example.bookcatalog.repository.BookRepository;

import java.util.List;

public interface SearchStrategy {
    List<Book> search(String query, BookRepository repository);
}
