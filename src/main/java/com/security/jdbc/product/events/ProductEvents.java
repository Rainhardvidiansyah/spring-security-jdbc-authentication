package com.security.jdbc.product.events;

import org.springframework.context.ApplicationEvent;

public class ProductEvents extends ApplicationEvent {

    private Long id;

    private String name;

    private String description;

    private int stockQuantity;


    public ProductEvents(Object source, Long id, String name, String description, int stockQuantity) {
        super(source);
        this.id = id;
        this.name = name;
        this.description = description;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
}
