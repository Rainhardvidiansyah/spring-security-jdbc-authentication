package com.security.jdbc.product.repository;

import com.security.jdbc.product.dto.request.CreateProductDtoRequest;
import com.security.jdbc.product.dto.response.ProductResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    //TODO: ADD SAVE PRODUCT
    public CreateProductDtoRequest insertProduct(CreateProductDtoRequest productDtoRequest){
        LOGGER.info("INSERT PRODUCT METHOD IS HIT");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO products (name, sku, description, price, stock_quantity) VALUES (?, ?, ?, ?)";

        try {

            int row = jdbcTemplate.update(
                    connection -> {
                        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1, productDtoRequest.getName());
                        preparedStatement.setString(2, productDtoRequest.getSku());
                        preparedStatement.setString(3, productDtoRequest.getDescription());
                        preparedStatement.setDouble(4, productDtoRequest.getPrice());
                        preparedStatement.setInt(5, productDtoRequest.getStockQuantity());
                        return preparedStatement;
                    }, keyHolder);

            Number key = keyHolder.getKey();
            LOGGER.info("CONTENT OF KEY: {}", key);

            if (row == 1) {
                LOGGER.info("PRODUCT INSERTED WITH SKU: {}", productDtoRequest.getSku());
                Long productId = Objects.requireNonNull(keyHolder.getKey()).longValue();
                LOGGER.info("PRODUCT ID IS: {}", productId);
                return productDtoRequest;
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            LOGGER.error("DB ERROR!");
            throw new RuntimeException(e);
        }
    }



    //TODO: GET ALL PRODUCT
    public List<ProductResponseDto> getAllProducts(){
        LOGGER.info("GET ALL PRODUCTS IS HIT");
        String sql = """
                    SELECT p.id AS id, p.name AS name, p.sku AS sku, p.description AS description,
                    p.price AS price, p.stock_quantity AS quantity FROM products p
                    """;
        try {
            return jdbcTemplate.query(sql, (rs, rw) -> {
                ProductResponseDto productResponse = new ProductResponseDto();
                productResponse.setId(rs.getLong("id"));
                productResponse.setName(rs.getString("name"));
                productResponse.setSku(rs.getString("sku"));
                productResponse.setDescription(rs.getString("description"));
                productResponse.setPrice(rs.getDouble("price"));
                productResponse.setStockQuantity(rs.getInt("quantity"));
                return productResponse;
            });
        }catch (DataAccessException e){
            LOGGER.error("ERROR IN GET ALL PRODUCTS IS: {}", e.getMessage());
            throw e;
        }
    }


    //TODO: GET PRODUCT BY ID







    //TODO: GET PRODUCT BY NAME

    //TODO: DYNAMIC QUERY FILTER
//    public void getAllProducts(String name, int price){
//        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
//
//        List<String> params = new ArrayList<>();
//
//        if(name!=null || !name.isEmpty()){
//            sql.append("AND name LIKE ?");
//            params.add("% name %");
//        }
//    }
}


//read: https://docs.spring.io/spring-framework/docs/4.3.20.RELEASE/spring-framework-reference/html/jdbc.html