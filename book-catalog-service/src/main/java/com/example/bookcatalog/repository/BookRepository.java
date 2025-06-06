package com.example.bookcatalog.repository;

import com.example.bookcatalog.model.entry.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorContaining(String author);

    List<Book> findByTitleContaining(String title);

    Optional<Book> findByIsbn(String isbn);

}
