package com.coffeeteam.tutuplapak.product.dto;

import com.coffeeteam.tutuplapak.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record ProductResponse(
        Long productId,
        String name,
        String category,
        Integer qty,
        Integer price,
        String sku,
        Long fileId,
        String fileUri,
        String fileThumbnailUri,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                StringUtils.capitalize(product.getCategory().name().toLowerCase()),
                product.getQty(),
                product.getPrice(),
                product.getSku(),
                product.getImage().getId(),
                product.getImage().getUri(),
                product.getImage().getThumbnailUri(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
