package com.coffeeteam.tutuplapak.core.entity;


import com.coffeeteam.tutuplapak.file.model.Image;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

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
    @Builder.Default()
    private String bankAccountName = "";

    @Column(name = "bank_account_holder", nullable = false)
    @Builder.Default()
    private String bankAccountHolder = "";

    @Column(name = "bank_account_number", nullable = false)
    @Builder.Default()
    private String bankAccountNumber = "";

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
