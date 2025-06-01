package com.security.jdbc.products;

import com.security.jdbc.product.dto.request.CreateProductDtoRequest;
import com.security.jdbc.product.dto.response.ProductResponseDto;
import com.security.jdbc.product.repository.ProductRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {


    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProductRepositoryImpl productRepositoryImpl;

    @Test
    void insertProduct_successfulInsert_returnsDto() {
        // Arrange
        CreateProductDtoRequest request = new CreateProductDtoRequest();
        request.setName("Test Product");
        request.setSku("SKU123");
        request.setDescription("Description");
        request.setPrice(100.0);
        request.setStockQuantity(10);

        Mockito.when(jdbcTemplate.update(Mockito.any(PreparedStatementCreator.class))).thenReturn(1);

        // Act
        CreateProductDtoRequest result = productRepositoryImpl.insertProduct(request);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test Product", result.getName());
    }

    @Test
    void insertProduct_updateReturnsZero_returnsNull() {
        // Arrange
        CreateProductDtoRequest request = new CreateProductDtoRequest();
        request.setName("Test Product");
        request.setSku("SKU123");
        request.setDescription("Description");
        request.setPrice(100.0);
        request.setStockQuantity(10);

        Mockito.when(jdbcTemplate.update(Mockito.any(PreparedStatementCreator.class))).thenReturn(0);

        // Act
        CreateProductDtoRequest result = productRepositoryImpl.insertProduct(request);

        // Assert
        Assertions.assertNull(result);
    }

    @Test
    void insertProduct_dataAccessException_throwsRuntimeException() {
        // Arrange
        CreateProductDtoRequest request = new CreateProductDtoRequest();
        request.setName("Test Product");
        request.setSku("SKU123");
        request.setDescription("Description");
        request.setPrice(100.0);
        request.setStockQuantity(10);

        Mockito.when(jdbcTemplate.update(Mockito.any(PreparedStatementCreator.class)))
                .thenThrow(new DataAccessException("DB error") {});

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            productRepositoryImpl.insertProduct(request);
        });
    }



    @Test
    void insertProduct_shouldThrowException_whenJdbcFails() {
        CreateProductDtoRequest request = new CreateProductDtoRequest();
        request.setName("Test Product");
        request.setSku("SKU123");
        request.setDescription("Test Description");
        request.setPrice(99.00);
        request.setStockQuantity(10);
        when(jdbcTemplate.update(ArgumentMatchers.any(PreparedStatementCreator.class),
                        ArgumentMatchers.any(KeyHolder.class)))
                .thenThrow(new DataAccessException("DB error") {});

        assertThrows(RuntimeException.class, () -> productRepositoryImpl.insertProduct(request));
    }


    @Test
    void testInsert_thenThrowProductThrowsException() {
        // Arrange
        CreateProductDtoRequest request = new CreateProductDtoRequest();
        request.setName("Test Product");

        when(jdbcTemplate.update(ArgumentMatchers.any(PreparedStatementCreator.class), ArgumentMatchers.any(KeyHolder.class)))
                .thenThrow(new DataAccessException("DB error") {});

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productRepositoryImpl.insertProduct(request));
    }

    @Test
    void testGetAllProducts_returnsProductList() {
        // Arrange
        ProductResponseDto mockProduct = new ProductResponseDto();
        mockProduct.setId(1L);
        mockProduct.setName("Laptop");
        mockProduct.setSku("SKU123");
        mockProduct.setDescription("High-end laptop");
        mockProduct.setPrice(1500.0);
        mockProduct.setStockQuantity(10);

        List<ProductResponseDto> expectedList = List.of(mockProduct);

        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
                .thenReturn(expectedList);

        // Act
        List<ProductResponseDto> actualList = productRepositoryImpl.getAllProducts();

        // Assert
        Assertions.assertNotNull(actualList);
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals("Laptop", actualList.get(0).getName());
        Assertions.assertEquals("SKU123", actualList.get(0).getSku());
    }

    @Test
    void testGetAllProduct_thenThrowException(){
        ProductResponseDto mockProduct = new ProductResponseDto();
        mockProduct.setId(1L);
        mockProduct.setName("Laptop");
        mockProduct.setSku("SKU123");
        mockProduct.setDescription("High-end laptop");
        mockProduct.setPrice(1500.0);
        mockProduct.setStockQuantity(10);

        List<ProductResponseDto> expectedList = List.of(mockProduct);

        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
                .thenThrow(new DataAccessException("DB Error") {});

        Assertions.assertThrows(DataAccessException.class, () -> productRepositoryImpl.getAllProducts());
    }

}