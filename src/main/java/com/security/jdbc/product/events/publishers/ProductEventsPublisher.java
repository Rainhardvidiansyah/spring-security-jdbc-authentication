package com.security.jdbc.product.events.publishers;

import org.springframework.context.ApplicationEventPublisher;

public class ProductEventsPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public ProductEventsPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
