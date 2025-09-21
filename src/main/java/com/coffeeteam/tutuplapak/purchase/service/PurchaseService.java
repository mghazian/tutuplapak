package com.coffeeteam.tutuplapak.purchase.service;


import com.coffeeteam.tutuplapak.core.entity.*;
import com.coffeeteam.tutuplapak.core.repository.*;
import com.coffeeteam.tutuplapak.file.exception.FileNotFoundException;
import com.coffeeteam.tutuplapak.file.model.Image;
import com.coffeeteam.tutuplapak.file.repository.ImageRepository;
import com.coffeeteam.tutuplapak.purchase.dto.PaymentProofRequest;
import com.coffeeteam.tutuplapak.purchase.dto.PurchaseRequest;
import com.coffeeteam.tutuplapak.purchase.dto.PurchaseResponse;
import com.coffeeteam.tutuplapak.purchase.dto.PurchasedItemRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public PurchaseResponse createPurchase(PurchaseRequest request) {
        Purchase purchase = new Purchase();
        purchase = purchaseRepository.save(purchase);

        List<String> productIds = request.getPurchasedItems().stream()
                .map(PurchasedItemRequest::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllByIds(productIds);
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<PurchaseItem> purchaseItems = new ArrayList<>();
        for (PurchasedItemRequest purchasedItemRequest : request.getPurchasedItems()) {
            Product product = productMap.get(Long.parseLong(purchasedItemRequest.getProductId()));
            if (product != null ) {
                PurchaseItem purchaseItem = PurchaseItem.builder()
                        .purchaseId(purchase.getId())
                        .productId(product.getId())
                        .name(product.getName())
                        .category(product.getCategory().getDisplayName())
                        .qty(purchasedItemRequest.getQty())
                        .price(product.getPrice())
                        .sku(product.getSku())
                        .imageId(product.getImageId())
                        .imageUri(getImageUri(product.getImageId()))
                        .imageThumbnailUri(getImageThumbnailUri(product.getImageId()))
                        .build();
                purchaseItems.add(purchaseItem);
            }
        }

        purchaseItems = purchaseItemRepository.saveAll(purchaseItems);

        for (PurchasedItemRequest itemRequest : request.getPurchasedItems()) {
            Product product = productMap.get(Long.parseLong(itemRequest.getProductId()))    ;
            if (product != null) {
                product.setQty(product.getQty() - itemRequest.getQty());
                productRepository.save(product);
            }
        }

        return buildPurchaseResponse(purchase, purchaseItems);
    }

    private String getImageUri(Long imageId) {
        if (imageId == null) {
            return "";
        }

        return imageRepository.findById(imageId)
                .map(Image::getUri)
                .orElse("");
    }

    private String getImageThumbnailUri(Long imageId) {
        if (imageId == null) {
            return "";
        }

        return imageRepository.findById(imageId)
                .map(Image::getThumbnailUri)
                .orElse("");
    }

    private PurchaseResponse buildPurchaseResponse(Purchase purchase, List<PurchaseItem> purchaseItems) {
        // Hitung total harga
        Integer totalPrice = purchaseItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQty())
                .sum();

        // Buat purchased items DTO
        List<PurchaseResponse.PurchaseItemDto> purchaseItemsDto = purchaseItems.stream()
                .map(item -> PurchaseResponse.PurchaseItemDto.builder()
                        .productId(String.valueOf(item.getProductId()))
                        .name(item.getName())
                        .category(item.getCategory())
                        .qty(item.getQty())
                        .price(item.getPrice())
                        .sku(item.getSku())
                        .fileId(item.getImageId() != null ? String.valueOf(item.getImageId()) : "")
                        .fileUri(item.getImageUri())
                        .fileThumbnailUri(item.getImageThumbnailUri())
                        .createdAt(item.getCreatedAt())
                        .updatedAt(item.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        // Kelompokkan berdasarkan owner dan hitung total per seller
        Map<Long, List<PurchaseItem>> itemsByOwner = purchaseItems.stream()
                .collect(Collectors.groupingBy(PurchaseItem::getProductId));

        // Ambil owner IDs
        Set<Long> ownerIds = purchaseItems.stream()
                .map(PurchaseItem::getProductId)
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Product::getOwnerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<User> sellers = userRepository.findAllById(ownerIds);
        Map<Long, User> sellerMap = sellers.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // Buat payment details
        List<PurchaseResponse.PaymentDetailDto> paymentDetails = new ArrayList<>();
        Map<Long, Integer> sellerTotalMap = new HashMap<>();

        for (PurchaseItem item : purchaseItems) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                Long ownerId = product.getOwnerId();
                if (ownerId != null) {
                    sellerTotalMap.merge(ownerId, item.getPrice() * item.getQty(), Integer::sum);
                }
            }
        }

        for (Map.Entry<Long, Integer> entry : sellerTotalMap.entrySet()) {
            Long ownerId = entry.getKey();
            Integer sellerTotal = entry.getValue();

            User seller = sellerMap.get(ownerId);
            if (seller != null) {
                PurchaseResponse.PaymentDetailDto paymentDetail = PurchaseResponse.PaymentDetailDto.builder()
                        .bankAccountName(seller.getBankAccountName())
                        .bankAccountHolder(seller.getBankAccountHolder())
                        .bankAccountNumber(seller.getBankAccountNumber())
                        .totalPrice(sellerTotal)
                        .build();
                paymentDetails.add(paymentDetail);
            }
        }

        return PurchaseResponse.builder()
                .purchaseId(String.valueOf(purchase.getId()))
                .purchasedItems(purchaseItemsDto)
                .totalPrice(totalPrice)
                .paymentDetails(paymentDetails)
                .build();
    }

    public void uploadPaymentProof(String purchaseId, PaymentProofRequest request) {
        long purchaseIdLong;
        try {
            purchaseIdLong = Long.parseLong(purchaseId);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Purchase not found");
        }
     
        Purchase purchase = purchaseRepository.findById(purchaseIdLong)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

        List<Long> imageIds = request.getFileIds().stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<Image> paymentProofImages = imageRepository.findAllById(imageIds);

        if (paymentProofImages.size() != imageIds.size()) {
            throw new IllegalArgumentException("One or more payment proof images not found");
        }

        decreaseProductQuantities(purchase);
    }

    private void decreaseProductQuantities(Purchase purchase) {
        List<PurchaseItem> purchaseItems = purchaseItemRepository.findByPurchaseId(purchase.getId());

        for (PurchaseItem item : purchaseItems) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                // Kurangi quantity (bisa sampai negatif sesuai requirement)
                product.setQty(product.getQty() - item.getQty());
                productRepository.save(product);
            }
        }
    }
}