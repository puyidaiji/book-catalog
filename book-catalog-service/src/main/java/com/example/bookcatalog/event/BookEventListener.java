package com.example.bookcatalog.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {
    private static final Logger logger = LoggerFactory.getLogger(BookEventListener.class);

    @EventListener
    public void handleBookEvent(BookEvent event) {
        logger.info("Received book {} event - Book: {}", event.getEventType(), event.getBook());
        // 图书事件的处理逻辑，如发送通知、更新缓存等
    }
}
