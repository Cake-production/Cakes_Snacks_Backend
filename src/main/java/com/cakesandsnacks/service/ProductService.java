package com.cakesandsnacks.service;

import com.cakesandsnacks.dto.ProductDTO;
import com.cakesandsnacks.dto.ProductCreateDTO;
import com.cakesandsnacks.entity.Product;
import com.cakesandsnacks.entity.ProductCategory;
import com.cakesandsnacks.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductDTO createProduct(ProductCreateDTO dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(ProductCategory.valueOf(dto.getCategory()))
                .badge(dto.getBadge())
                .imageUrl(dto.getImageUrl())
                .isActive(true)
                .rating(0)
                .reviewCount(0)
                .sales(0)
                .build();

        Product saved = productRepository.save(product);
        return convertToDTO(saved);
    }
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(ProductCategory.valueOf(category))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getTopSellingProducts() {
        return productRepository.findTopSellingProducts()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getLatestProducts() {
        return productRepository.findLatestProducts()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, ProductCreateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(ProductCategory.valueOf(dto.getCategory()));
        product.setBadge(dto.getBadge());
        product.setImageUrl(dto.getImageUrl());
        
        Product updated = productRepository.save(product);
        return convertToDTO(updated);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsActive(false);
        productRepository.save(product);
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().toString(),
                product.getBadge(),
                product.getImageUrl(),
                product.getRating(),
                product.getReviewCount(),
                product.getSales(),
                product.getCreatedAt()
        );
    }
}
