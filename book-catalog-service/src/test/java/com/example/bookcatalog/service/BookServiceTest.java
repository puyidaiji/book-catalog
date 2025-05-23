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

import static org.assertj.core.api.Assertions.assertThat;
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
}
