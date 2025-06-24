package com.example.bookcatalog.event;

import com.example.bookcatalog.model.entry.Book;
import org.springframework.context.ApplicationEvent;

public class BookEvent extends ApplicationEvent {
    private final Book book;
    private final String eventType;

    public BookEvent(Object source, Book book, String eventType) {
        super(source);
        this.book = book;
        this.eventType = eventType;
    }

    public Book getBook() {
        return book;
    }

    public String getEventType() {
        return eventType;
    }
}