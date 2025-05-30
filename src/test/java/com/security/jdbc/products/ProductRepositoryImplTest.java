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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {


    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProductRepositoryImpl productRepositoryImpl;


    @Test
    void testInsertProductSuccess() {
        // Arrange
        CreateProductDtoRequest request = new CreateProductDtoRequest();
        request.setName("Test Product");
        request.setSku("SKU123");
        request.setDescription("Test Description");
        request.setPrice(99.00);
        request.setStockQuantity(10);

        ArgumentCaptor<PreparedStatementCreator> pscCaptor = ArgumentCaptor.forClass(PreparedStatementCreator.class);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        keyHolder.getKeyList().add(Collections.singletonMap("GENERATED_KEY", 1L));

        Mockito.when(jdbcTemplate.update(ArgumentMatchers.any(PreparedStatementCreator.class),
                ArgumentMatchers.any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Collections.singletonMap("id", 1L));
            return 1;
        });

        // Act
        CreateProductDtoRequest result = productRepositoryImpl.insertProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals("SKU123", result.getSku());
    }

    @Test
    void testInsertProductFails() {
        // Arrange
        CreateProductDtoRequest request = new CreateProductDtoRequest();
        request.setName("Test Product");
        request.setSku("SKU123");
        request.setDescription("Test Description");
        request.setPrice(100.000);
        request.setStockQuantity(10);

        Mockito.when(jdbcTemplate.update(
                ArgumentMatchers.any(PreparedStatementCreator.class),
                ArgumentMatchers.any(KeyHolder.class)))
                .thenReturn(0);

        // Act
        CreateProductDtoRequest result = productRepositoryImpl.insertProduct(request);

        // Assert
        assertNull(result);
    }



    @Test
    void testInsertProductThrowsException() {
        // Arrange
        CreateProductDtoRequest request = new CreateProductDtoRequest();
        request.setName("Test Product");

        Mockito.when(jdbcTemplate.update(ArgumentMatchers.any(PreparedStatementCreator.class), ArgumentMatchers.any(KeyHolder.class)))
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

        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
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

        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
                .thenThrow(new DataAccessException("DB Error") {});

        Assertions.assertThrows(DataAccessException.class, () -> productRepositoryImpl.getAllProducts());
    }

}