package com.example.bookcatalog.repository;

import com.example.bookcatalog.model.entry.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    void whenFindByAuthorContaining_thenReturnBooks() {
        // given
        Book book = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("1111111111")
                .build();
        bookRepository.save(book);

        // when
        List<Book> found = bookRepository.findByAuthorContaining("Josh");

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    void whenFindByIsbn_thenReturnBook() {
        // given
        String isbn = "1111111111";
        Book book = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn(isbn)
                .build();
        bookRepository.save(book);

        // when
        Optional<Book> found = bookRepository.findByIsbn(isbn);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getIsbn()).isEqualTo(isbn);
    }
}