package com.eCommerce.productService.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eCommerce.productService.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
    Optional<Product> findByName(String name);
    
    @Query(value = "SELECT * FROM product WHERE name LIKE :pattern", nativeQuery = true)
    List<Product> fuzzyLooseMatch(@Param("pattern") String pattern);
}