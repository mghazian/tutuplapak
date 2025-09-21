package com.coffeeteam.tutuplapak.purchase.controller;

import com.coffeeteam.tutuplapak.purchase.dto.PaymentProofRequest;
import com.coffeeteam.tutuplapak.purchase.dto.PurchaseRequest;
import com.coffeeteam.tutuplapak.purchase.dto.PurchaseResponse;
import com.coffeeteam.tutuplapak.purchase.service.PurchaseService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final MeterRegistry meterRegistry;

    private Counter purchaseCreatedCounter;
    private Counter paymentProofUploadedCounter;
    private Counter purchaseValidationErrorCounter;
    private Counter purchaseInternalServerErrorCounter;
    private Timer purchaseCreateTimer;
    private Timer paymentProofUploadTimer;

    @PostConstruct
    public void initMetrics() {
        this.purchaseCreatedCounter = Counter.builder("purchase_created_total")
                .description("Total purchases successfully created")
                .tag("status", "success")
                .register(meterRegistry);

        this.paymentProofUploadedCounter = Counter.builder("payment_proof_uploaded_total")
                .description("Total payment proofs successfully uploaded")
                .tag("status", "success")
                .register(meterRegistry);

        this.purchaseValidationErrorCounter = Counter.builder("purchase_errors_total")
                .description("Total purchase validation errors")
                .tag("type", "validation")
                .register(meterRegistry);

        this.purchaseInternalServerErrorCounter = Counter.builder("purchase_errors_total")
                .description("Total internal server errors during purchase")
                .tag("type", "internal")
                .register(meterRegistry);

        this.purchaseCreateTimer = Timer.builder("purchase_create_duration_seconds")
                .description("Duration of purchase creation")
                .register(meterRegistry);

        this.paymentProofUploadTimer = Timer.builder("payment_proof_upload_duration_seconds")
                .description("Duration of payment proof upload")
                .register(meterRegistry);
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@RequestBody @Valid PurchaseRequest request) {
        return purchaseCreateTimer.record(() -> {
            try {
                PurchaseResponse response = purchaseService.createPurchase(request);
                purchaseCreatedCounter.increment();
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (IllegalArgumentException e) {
                purchaseValidationErrorCounter.increment();
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                purchaseInternalServerErrorCounter.increment();
                throw e;
            }
        });
    }

    @PostMapping("/{purchaseId}")
    public ResponseEntity<Void> uploadPaymentProof(
            @PathVariable String purchaseId,
            @RequestBody @Valid PaymentProofRequest request) {

        return paymentProofUploadTimer.record(() -> {
            try {
                purchaseService.uploadPaymentProof(purchaseId, request);
                paymentProofUploadedCounter.increment();
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (EntityNotFoundException e) {
                purchaseValidationErrorCounter.increment();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (IllegalArgumentException e) {
                purchaseValidationErrorCounter.increment();
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                purchaseInternalServerErrorCounter.increment();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }
}