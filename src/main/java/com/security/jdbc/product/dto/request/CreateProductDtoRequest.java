package com.security.jdbc.product.dto.request;


public class CreateProductDtoRequest {
    private String name;
    private String sku;
    private String description;
    private double price;
    private int stockQuantity;

    public CreateProductDtoRequest() {
    }

    public CreateProductDtoRequest(String name, String sku, String description, double price, int stockQuantity) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "CreateProductDtoRequest{" +
                "name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
