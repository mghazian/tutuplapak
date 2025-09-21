package com.coffeeteam.tutuplapak.purchase.dto;

import com.coffeeteam.tutuplapak.core.deserializer.StrictStringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchasedItemRequest {

    @NotBlank(message = "productId is required")
    @JsonDeserialize(using = StrictStringDeserializer.class)
    private String productId;

    @NotNull(message = "qty is required")
    @Min(1)
    private Integer qty;
}