package com.example.bookcatalog.service;

import com.example.bookcatalog.event.BookEvent;
import com.example.bookcatalog.exception.ResourceNotFoundException;
import com.example.bookcatalog.factory.SearchStrategyFactory;
import com.example.bookcatalog.model.entry.Book;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.service.strategy.SearchStrategy;
import com.example.bookcatalog.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final SearchStrategyFactory searchStrategyFactory;

    private final ApplicationEventPublisher eventPublisher;

    private final BookValidator validator;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book addBook(Book book) throws ValidationException {
        validator.validate(book);
        Book savedBook = bookRepository.save(book);
        eventPublisher.publishEvent(new BookEvent(this, savedBook, "CREATED"));
        return savedBook;
    }

    public Book updateBook(Long id, Book bookDetails) {

        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setGenre(bookDetails.getGenre());
                    book.setPublicationYear(bookDetails.getPublicationYear());
                    Book updatedBook = bookRepository.save(book);
                    eventPublisher.publishEvent(new BookEvent(this, updatedBook, "UPDATED"));
                    return updatedBook;
                })
                .orElse(null);

    }

    public void deleteBook(Long id) {

        bookRepository.findById(id).ifPresent(book -> {
            bookRepository.deleteById(id);
            eventPublisher.publishEvent(new BookEvent(this, book, "DELETED"));
        });
    }

    public List<Book> searchBooks(String type, String query) {
        SearchStrategy strategy = searchStrategyFactory.getStrategy(type);
        return strategy.search(query, bookRepository);
    }
}
