package com.coffeeteam.tutuplapak.core.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String email;
    private String password;

    @Column(name = "bank_account_name", nullable = false)
    private String bankAccountName = "";

    @Column(name = "bank_account_holder", nullable = false)
    private String bankAccountHolder = "";

    @Column(name = "bank_account_number", nullable = false)
    private String bankAccountNumber = "";

    @Column(name = "image_id")
    private Long imageId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // Relasi ke Image
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "fk_users_image_id"), insertable = false, updatable = false)
    private Image image;

    // Relasi ke Product
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    // Relasi ke Image (sebagai owner)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images;
}