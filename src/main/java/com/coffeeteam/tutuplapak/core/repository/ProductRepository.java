package com.coffeeteam.tutuplapak.core.repository;
import com.coffeeteam.tutuplapak.core.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findAllByIds(@Param("productIds") List<String> productIds);

    List<Product> findByOwnerIdIn(List<Long> ownerIds);
}