package com.coffeeteam.tutuplapak.product.entity;

import com.coffeeteam.tutuplapak.core.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_image_user"))
    private User owner;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private String thumbnailUri;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}