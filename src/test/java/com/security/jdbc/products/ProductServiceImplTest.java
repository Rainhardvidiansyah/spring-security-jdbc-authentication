package com.security.jdbc.products;

import com.security.jdbc.product.dto.request.CreateProductDtoRequest;
import com.security.jdbc.product.repository.ProductRepository;
import com.security.jdbc.product.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
class ProductServiceTest {


    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void saveProduct() {
        CreateProductDtoRequest productDtoRequest = new CreateProductDtoRequest();
        productDtoRequest.setName("Product 1");
        productDtoRequest.setDescription("Product 1 description");
        productDtoRequest.setSku("123456789");
        productDtoRequest.setPrice(99.0001);

        Mockito.when(productRepository.insertProduct(productDtoRequest))
                .thenReturn(productDtoRequest);

       // ProductService service = new ProductService(productRepository);

        CreateProductDtoRequest savedProduct = productService.saveProduct(productDtoRequest);

        Assertions.assertNotNull(savedProduct);

        Mockito.verify(productRepository, Mockito.times(1))
                .insertProduct(productDtoRequest);
    }
}