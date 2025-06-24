package com.example.bookcatalog.controller;

import com.example.bookcatalog.model.entry.Book;
import com.example.bookcatalog.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void whenGetAllBooks_thenReturnJsonArray() throws Exception {
        // given
        Book book = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .build();
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookService.getAllBooks()).thenReturn(books);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void whenInvalidSearchType_thenReturnBadRequest() throws Exception {
        // given
        when(bookService.searchBooks(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid search type"));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/search")
                .param("type", "invalid")
                .param("query", "test"))
                .andExpect(status().isBadRequest());
    }

}
