package com.coffeeteam.tutuplapak.product.dto;

import lombok.Data;

@Data
public class ProductGetRequest {
    private Integer limit = 5;
    private Integer offset = 0;
    private String productId;
    private String sku;

    private String category;
    private String sortBy;
}
