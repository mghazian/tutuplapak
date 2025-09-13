package com.coffeeteam.tutuplapak.core.entity;

import java.time.OffsetDateTime;

import com.coffeeteam.tutuplapak.file.model.Image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or AUTO, SEQUENCE, etc.
    private Long id;
    private String phone;
    private String email;
    private String password;
    private Long imageId;
    @Column(nullable = false)
    private String bankAccountName = "";
    @Column(nullable = false)
    private String bankAccountHolder = "";
    @Column(nullable = false)
    private String bankAccountNumber = "";
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

}
