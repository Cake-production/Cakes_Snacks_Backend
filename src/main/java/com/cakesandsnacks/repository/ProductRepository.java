package com.cakesandsnacks.repository;

import com.cakesandsnacks.entity.Product;
import com.cakesandsnacks.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// Product Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);
    List<Product> findByIsActiveTrue();
    List<Product> findByNameContainingIgnoreCase(String name);
    @Query("SELECT p FROM Product p ORDER BY p.sales DESC LIMIT 10")
    List<Product> findTopSellingProducts();
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC LIMIT 6")
    List<Product> findLatestProducts();
}
