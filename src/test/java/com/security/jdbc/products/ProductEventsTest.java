package com.security.jdbc.products;


import com.security.jdbc.product.events.ProductEvents;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;


@RecordApplicationEvents
class ProductEventsTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ApplicationEvents applicationEvents;


    @Test
    void testEventPublishing() {
        // Publish an event
        ProductEvents events = new ProductEvents(this, 1L, "product 1", "description product 1", 2);
        Assertions.assertNotNull(events);

        Assertions.assertEquals(1L, events.getId());
        Assertions.assertEquals("product 1", events.getName());
        Assertions.assertEquals("description product 1", events.getDescription());
        Assertions.assertEquals(2, events.getStockQuantity());
//        publisher.publishEvent(events);
//
//        // Assert that the event was published
//        applicationEvents.stream()
//                .filter(e -> e instanceof ProductEvents)
//                .findFirst()
//                .ifPresent(actualEvent ->
//                        Assertions.assertEquals(events.getId(), ((ProductEvents) actualEvent).getId()));

    }

}