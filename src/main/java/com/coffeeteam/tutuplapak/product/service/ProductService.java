package com.coffeeteam.tutuplapak.product.service;

import com.coffeeteam.tutuplapak.core.entity.Product;
import com.coffeeteam.tutuplapak.core.repository.ProductRepository;
import com.coffeeteam.tutuplapak.file.exception.FileNotFoundException;
import com.coffeeteam.tutuplapak.file.model.Image;
import com.coffeeteam.tutuplapak.file.repository.ImageRepository;
import com.coffeeteam.tutuplapak.product.dto.ProductCreateRequest;
import com.coffeeteam.tutuplapak.product.dto.ProductGetRequest;
import com.coffeeteam.tutuplapak.product.dto.ProductResponse;
import com.coffeeteam.tutuplapak.product.spesification.ProductSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ProductService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public ProductResponse createProduct(Long userId, ProductCreateRequest request) {
        long fileId;
        try {
            System.out.println("masuk fileid parse" + request.getFileId());
            fileId = Long.parseLong(request.getFileId());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File Not Exist or You are not the owner");
        }

        Image image = imageRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "File Not Exist or You are not the owner"));

        if (productRepository.existsByOwnerIdAndSku(userId, request.sku())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SKU already exists for this user");
        }


        ZonedDateTime now = ZonedDateTime.now();

        Product product = Product.builder().
                name(request.name()).
                category(request.category()).
                price(request.price()).
                qty(request.qty()).
                sku(request.sku()).
                image(image).
                ownerId(userId).
                createdAt(now).
                updatedAt(now).
                build();

        productRepository.save(product);

        return ProductResponse.fromEntity(product);
    }

    @Override
    public List<ProductResponse> getAllProducts(ProductGetRequest request) {
        Specification<Product> spec = ProductSpecification.build(request);
        Pageable pageable = PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit(), resolveSort(request.getSortBy()));
        return productRepository.findAll(spec, pageable)
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Override
    public ProductResponse update(Long UserId, String productId, ProductCreateRequest request) {
        long productIdLong;
        try {
            productIdLong = Long.parseLong(productId);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }

        Product product = productRepository.findByIdAndOwnerId(productIdLong, UserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));

        if (!product.getSku().equals(request.sku()) &&
                productRepository.existsByOwnerIdAndSku(product.getOwnerId(), request.sku())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SKU already exists for this user");
        }

        long fileId;
        try {
            System.out.println("masuk fileid parse" + request.getFileId());
            fileId = Long.parseLong(request.getFileId());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File Not Exist or You are not the owner");
        }

        Image image = imageRepository.findById(fileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "File Not Exist"));

        product.setName(request.name());
        product.setCategory(request.category());
        product.setPrice(request.price());
        product.setQty(request.qty());
        product.setSku(request.sku());
        product.setImage(image);
        product.setUpdatedAt(ZonedDateTime.now());
        productRepository.save(product);
        return ProductResponse.fromEntity(product);
    }

    @Override
    public void delete(Long UserId, Long productId) {
        Product product = productRepository.findByIdAndOwnerId(productId, UserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found or You are not the owner"));
        productRepository.delete(product);
    }

    private Sort resolveSort(String sortBy) {
        if ("cheapest".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.ASC, "price");
        } else if ("expensive".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "price");
        } else if ("newest".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "updatedAt", "createdAt");
        } else if ("oldest".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.ASC, "updatedAt", "createdAt");
        }
        return Sort.unsorted();
    }
}
