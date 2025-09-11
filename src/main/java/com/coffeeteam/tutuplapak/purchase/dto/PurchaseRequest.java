package com.coffeeteam.tutuplapak.purchase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseRequest {

    @Valid
    @NotNull(message = "purchasedItems cannot be null")
    @Size(min = 1, message = "purchasedItems must have at least 1 item")
    private List<@Valid PurchasedItemRequest> purchasedItems;

    @NotBlank(message = "senderName is required")
    @Size(min = 4, max = 55, message = "senderName must be between 4 and 55 characters")
    private String senderName;

    @NotBlank(message = "senderContactType is required")
    @Pattern(regexp = "^(email|phone)$", message = "senderContactType must be either 'email' or 'phone'")
    private String senderContactType;

    @NotBlank(message = "senderContactDetail is required")
    private String senderContactDetail;
}