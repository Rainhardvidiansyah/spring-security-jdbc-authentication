package com.security.jdbc.products;



import com.security.jdbc.product.dto.request.CreateProductDtoRequest;
import com.security.jdbc.product.dto.response.ProductResponseDto;
import com.security.jdbc.product.repository.ProductRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@ActiveProfiles("test")
public class ProductRepositoryImplIntegrationTest {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepositoryImpl productRepositoryImpl;

    @Test
    public void testInsertProduct_success() {
        CreateProductDtoRequest dto = new CreateProductDtoRequest();
        dto.setName("Produk Test");
        dto.setSku("SKU123");
        dto.setDescription("Deskripsi test");
        dto.setPrice(100.0);
        dto.setStockQuantity(50);

        var result = productRepositoryImpl.insertProduct(dto);

        assertNotNull(result);
        assertEquals("Produk Test", result.getName());
        assertEquals("SKU123", result.getSku());
        assertEquals(100.0, result.getPrice());
        assertEquals(50, result.getStockQuantity());
    }

    @Test
    void testGetAllProducts() {
       //To all my brothers, don't run this sql query!
//        jdbcTemplate.update("""
//            INSERT INTO products (name, sku, description, price, stock_quantity, created_at)
//            VALUES ('Product B', 'SKU-B1', 'Desc', 15.0, 5, CURRENT_TIMESTAMP)
//        """);

        List<ProductResponseDto> products = productRepositoryImpl.getAllProducts();

//        assertThat(products)

        assertThat(products.get(0).getSku()).isEqualTo("SKU123");
    }

}



