package com.coffeeteam.tutuplapak.core.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "purchase_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "product_id")
    private Long productId;

    private String name;
    private String category;
    private Integer qty;
    private Integer price;
    private String sku;

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_uri")
    private String imageUri;

    @Column(name = "image_thumbnail_uri")
    private String imageThumbnailUri;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // Relasi ke Purchase
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", foreignKey = @ForeignKey(name = "fk_purchase_items_purchase_id"), insertable = false, updatable = false)
    private Purchase purchase;
}