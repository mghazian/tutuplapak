package com.coffeeteam.tutuplapak.core.repository;
import com.coffeeteam.tutuplapak.core.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findAllByIds(@Param("productIds") List<String> productIds);

    List<Product> findByOwnerIdIn(List<Long> ownerIds);

    boolean existsByOwnerIdAndSku(Long ownerId, String sku);
    Optional<Product> findByIdAndOwnerId(Long id, Long ownerId);
}