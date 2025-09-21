package com.coffeeteam.tutuplapak.product.service;

import com.coffeeteam.tutuplapak.product.dto.ProductCreateRequest;
import com.coffeeteam.tutuplapak.product.dto.ProductGetRequest;
import com.coffeeteam.tutuplapak.product.dto.ProductResponse;

import java.util.List;

public interface IProductService {
    ProductResponse createProduct(Long userId, ProductCreateRequest request);
    List<ProductResponse> getAllProducts(ProductGetRequest request);
    ProductResponse update(Long userId, String productId, ProductCreateRequest request);
    void delete(Long UserId, Long productId);
}
