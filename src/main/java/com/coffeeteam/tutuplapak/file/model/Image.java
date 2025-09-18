package com.coffeeteam.tutuplapak.file.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    Long ownerId;

    @Column
    String uri;

    @Column
    String thumbnailUri;

    @Column
    ZonedDateTime createdAt;

    @Column
    ZonedDateTime updatedAt;
}
