package com.coffeeteam.tutuplapak.product.repository;

import com.coffeeteam.tutuplapak.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByOwnerIdAndSku(Long ownerId, String sku);
    Optional<Product> findByIdAndOwnerId(Long id, Long ownerId);
}
