package com.security.jdbc.product.repository;

import com.security.jdbc.product.dto.request.CreateProductDtoRequest;
import com.security.jdbc.product.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductRepository {

    CreateProductDtoRequest insertProduct(CreateProductDtoRequest productDtoRequest);

    List<ProductResponseDto> getAllProducts();


}
