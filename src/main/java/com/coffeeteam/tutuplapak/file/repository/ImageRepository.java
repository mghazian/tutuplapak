package com.coffeeteam.tutuplapak.file.repository;

import com.coffeeteam.tutuplapak.file.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
