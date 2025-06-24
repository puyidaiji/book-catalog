package com.example.bookcatalog.validator;

import com.example.bookcatalog.model.entry.Book;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.xml.bind.ValidationException;

@Component
public class IsbnValidator implements BookValidator {
    private BookValidator next;

    @Override
    public void validate(Book book) throws ValidationException {
        if (!isValidIsbn(book.getIsbn())) {
            throw new ValidationException("Invalid ISBN");
        }
        if (next != null) {
            next.validate(book);
        }
    }

    @Override
    public void setNext(BookValidator next) {
        this.next = next;
    }

    private boolean isValidIsbn(String isbn) {
        return !StringUtils.isEmpty(isbn);
    }
}