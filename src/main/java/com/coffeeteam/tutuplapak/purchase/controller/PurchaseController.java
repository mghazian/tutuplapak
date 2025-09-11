package com.coffeeteam.tutuplapak.purchase.controller;


import com.coffeeteam.tutuplapak.purchase.dto.PaymentProofRequest;
import com.coffeeteam.tutuplapak.purchase.dto.PurchaseRequest;
import com.coffeeteam.tutuplapak.purchase.dto.PurchaseResponse;
import com.coffeeteam.tutuplapak.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<?> getPurchase() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "error", "Bad Request",
                        "message", "File size exceeds the allowed limit"
                ));
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@RequestBody PurchaseRequest request) {
        PurchaseResponse response = purchaseService.createPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{purchaseId}")
    public ResponseEntity<Void> uploadPaymentProof(
            @PathVariable Long purchaseId,
            @RequestBody @Valid PaymentProofRequest request) {
        try {
            purchaseService.uploadPaymentProof(purchaseId, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}