package com.coffeeteam.tutuplapak.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.Category;

public record ProductUpdateRequest(
        @NotBlank
        @Size(min = 4, max = 32)
        String name,

        @NotNull
        Category category,

        @NotNull
        @Min(1)
        Integer qty,

        @NotNull
        @Min(100)
        Integer price,

        @NotBlank
        @Size(max = 32)
        String sku,

        @NotBlank
        String Field
        ) {
}
