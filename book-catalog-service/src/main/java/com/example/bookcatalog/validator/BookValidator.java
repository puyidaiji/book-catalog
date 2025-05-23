package com.example.bookcatalog.validator;

import com.example.bookcatalog.model.entry.Book;

import javax.xml.bind.ValidationException;

public interface BookValidator {

    void validate(Book book) throws ValidationException;

    void setNext(BookValidator next);

}
