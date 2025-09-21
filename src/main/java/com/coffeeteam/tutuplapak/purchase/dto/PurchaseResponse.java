package com.coffeeteam.tutuplapak.purchase.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PurchaseResponse {
    private String purchaseId;
    private List<PurchaseItemDto> purchasedItems;
    private Integer totalPrice;
    private List<PaymentDetailDto> paymentDetails;

    @Getter
    @Setter
    @Builder
    public static class PurchaseItemDto {
        private String productId;
        private String name;
        private String category;
        private Integer qty;
        private Integer price;
        private String sku;
        private String fileId;
        private String fileUri;
        private String fileThumbnailUri;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class PaymentDetailDto {
        private String bankAccountName;
        private String bankAccountHolder;
        private String bankAccountNumber;
        private Integer totalPrice;
    }
}