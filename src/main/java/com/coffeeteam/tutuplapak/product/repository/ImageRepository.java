package com.coffeeteam.tutuplapak.product.repository;

import com.coffeeteam.tutuplapak.product.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
