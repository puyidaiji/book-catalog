package com.example.bookcatalog.service;

import com.example.bookcatalog.event.BookEvent;
import com.example.bookcatalog.factory.SearchStrategyFactory;
import com.example.bookcatalog.model.entry.Book;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.service.strategy.SearchStrategy;
import com.example.bookcatalog.validator.BookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookValidator validator;

    @Mock
    private SearchStrategyFactory searchStrategyFactory;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private BookService bookService;

    @Test
    void whenAddBook_thenPublishEvent() throws ValidationException {
        // given
        Book book = Book.builder()
                .title("Clean Code")
                .author("Robert Martin")
                .isbn("978-0132350884")
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // when
        Book savedBook = bookService.addBook(book);

        // then
        verify(eventPublisher).publishEvent(any(BookEvent.class));
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void whenSearchByTitle_thenUseTitleStrategy() {
        // given
        String query = "Clean";
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(Book.builder().title("Clean Code").build());
        expectedBooks.add(Book.builder().title("Clean Architecture").build());

        SearchStrategy mockStrategy = mock(SearchStrategy.class);
        when(mockStrategy.search(eq(query), any())).thenReturn(expectedBooks);
        when(searchStrategyFactory.getStrategy("title")).thenReturn(mockStrategy);

        // when
        List<Book> result = bookService.searchBooks("title", query);

        // then
        assertThat(result).hasSize(2);
        verify(mockStrategy).search(query, bookRepository);
    }

    @Test
    void getBookById_Found() {
        Long id = 1L;
        Book book = Book.builder().title("Title").author("Author").isbn("ISBN").genre("Genre").publicationYear(2020).build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(id);

        assertTrue(result.isPresent());
        assertEquals("Title", result.get().getTitle());
    }

    @Test
    void getBookById_NotFound() {
        Long id = 99L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(id);

        assertTrue(result.isPresent());
    }

    @Test
    void createBook() throws ValidationException {
        Book book = Book.builder().title("New Book").author("Author").isbn("ISBN").genre("Genre").publicationYear(2023).build();
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.addBook(book);

        assertNotNull(savedBook);
    }

    @Test
    void updateBook_Success() {
        Long id = 1L;
        Book existingBook = Book.builder().title("Old Title").author("Author").isbn("ISBN").genre("Genre").publicationYear(2020).build();
        Book updatedDetails = Book.builder().title("New Title").author("New Author").isbn("New ISBN").genre("New Genre").publicationYear(2021).build();

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.updateBook(id, updatedDetails);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
    }

    @Test
    void updateBook_NotFound() {
        Long id = 99L;
        Book updatedDetails = Book.builder().title("Title").author("Author").isbn("ISBN").genre("Genre").publicationYear(2021).build();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Book result = bookService.updateBook(id, updatedDetails);

        assertNull(result);
    }

    @Test
    void deleteBook() {
        Long id = 1L;
        Book book = Book.builder().title("Title").author("Author").isbn("ISBN").genre("Genre").publicationYear(2020).build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        bookService.deleteBook(id);

        verify(bookRepository).deleteById(id);
    }
}
