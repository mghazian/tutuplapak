package com.coffeeteam.tutuplapak.product.spesification;

import com.coffeeteam.tutuplapak.product.dto.ProductGetRequest;
import com.coffeeteam.tutuplapak.product.entity.Product;
import com.coffeeteam.tutuplapak.product.enums.Category;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> build(ProductGetRequest params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.getProductId() != null)
                predicates.add(cb.equal(root.get("id"), params.getProductId()));
            if (params.getSku() != null)
                predicates.add(cb.equal(root.get("sku"), params.getSku()));
            if (params.getCategory() != null) {
                try {
                    Category categoryEnum = Category.valueOf(params.getCategory().toUpperCase());
                    predicates.add(cb.equal(root.get("category"), categoryEnum));
                } catch (IllegalArgumentException ignored) {

                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

