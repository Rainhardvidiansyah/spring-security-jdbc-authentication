## Recommended structure folder


````
product/
├── controller/
│   └── ProductController.java
│       # Handles HTTP requests related to products (RESTful endpoints).
│       # Delegates the logic to ProductService.
│
├── service/
│   ├── ProductService.java
│       # Interface defining business operations related to products.
│   └── ProductServiceImpl.java
│       # Concrete implementation of ProductService.
│       # Typically contains business logic and orchestration.
│
├── repository/
│   ├── ProductRepository.java
│       # Abstraction for data access operations.
│   └── ProductRepositoryImpl.java
│       # JdbcTemplate-based implementation of the ProductRepository.
│       # Interacts directly with the database using raw SQL.
│
├── model/
│   └── Product.java
│       # Domain model / entity class representing a product.
│       # Can be used with ORM or manually mapped with RowMapper.
│
├── dto/
│   ├── ProductRequest.java
│       # Data Transfer Object used for rec

````

## Example for all modules including utils and/or helper

````
src/
├── main/java/com/security/
│   ├── product/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── exception/
│   │   └── ProductModule.java   # Config for product module
│   ├── user/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── exception/
│   │   └── UserModule.java      # Config for user module
│   ├── order/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── exception/
│   │   └── OrderModule.java     # Config for order mdoule
│   ├── utils/
│   │   ├── date/
│   │   ├── string/
│   │   ├── validation/
│   │   └── CommonUtils.java     # utils
│   └── Application.java         # Main Application Entry
└── resources/
    └── application.properties   # Global Configuration
````


