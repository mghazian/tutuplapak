package com.coffeeteam.tutuplapak.purchase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchasedItemRequest {

    @NotBlank(message = "productId is required")
    private String productId;

    @NotNull(message = "qty is required")
    @Min(value = 2, message = "qty must be at least 2")
    private Integer qty;
}