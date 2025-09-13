package com.coffeeteam.tutuplapak.product.entity;

import com.coffeeteam.tutuplapak.core.entity.User;
import com.coffeeteam.tutuplapak.file.model.Image;
import com.coffeeteam.tutuplapak.product.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Entity(name = "products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Integer qty;
    private Integer price;
    private String sku;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;



    @OneToOne(optional = true)
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "fk_product_image"))
    private Image image;

}
