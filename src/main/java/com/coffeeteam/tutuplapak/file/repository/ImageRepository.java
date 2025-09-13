package com.coffeeteam.tutuplapak.file.repository;

import com.coffeeteam.tutuplapak.file.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByIdAndOwnerId(Long id, Long ownerId);
}
