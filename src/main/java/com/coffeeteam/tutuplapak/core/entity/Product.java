package com.coffeeteam.tutuplapak.core.entity;

import com.coffeeteam.tutuplapak.file.model.Image;
import com.coffeeteam.tutuplapak.product.enums.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String sku;

    @Column(name = "image_id")
    private Long imageId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // Relasi ke User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_products_owner_id"), insertable = false, updatable = false)
    private User owner;

    // Relasi ke Image
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "fk_products_image_id"), insertable = false, updatable = false)
    private Image image;
}