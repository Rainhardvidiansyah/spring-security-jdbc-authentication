package com.security.jdbc.product.service;

import com.security.jdbc.product.dto.request.CreateProductDtoRequest;

public interface ProductService {

    CreateProductDtoRequest saveProduct(CreateProductDtoRequest productDtoRequest);
}
