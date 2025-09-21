package com.coffeeteam.tutuplapak.product.controller;

import com.coffeeteam.tutuplapak.auth.security.CustomUserDetails;
import com.coffeeteam.tutuplapak.product.dto.ProductCreateRequest;
import com.coffeeteam.tutuplapak.product.dto.ProductGetRequest;
import com.coffeeteam.tutuplapak.product.dto.ProductResponse;
import com.coffeeteam.tutuplapak.product.service.IProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/product")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@AuthenticationPrincipal CustomUserDetails claim, @Valid @RequestBody ProductCreateRequest request) {
        ProductResponse response = productService.createProduct(claim.user().getId(),request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> get(@ModelAttribute ProductGetRequest request) {
        List<ProductResponse> responses = productService.getAllProducts(request);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable @NotNull String productId,
            @Valid @RequestBody ProductCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails claim
    ) {
        ProductResponse response = productService.update(claim.user().getId(), productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long productId, @AuthenticationPrincipal CustomUserDetails claim) {
        productService.delete(claim.user().getId(),productId);
        return ResponseEntity.ok().build();
    }
}
