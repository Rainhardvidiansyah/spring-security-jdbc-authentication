package com.security.jdbc.product.service;


import com.security.jdbc.product.dto.request.CreateProductDtoRequest;
import com.security.jdbc.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public CreateProductDtoRequest saveProduct(CreateProductDtoRequest productDtoRequest){
        CreateProductDtoRequest productRequest = productRepository.insertProduct(productDtoRequest);
        return productRequest;
    }
}
