package com.eCommerce.productService.controller;



import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.eCommerce.productService.entity.Product;
import com.eCommerce.productService.service.ProductService;

import org.springframework.data.domain.Page;


import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return service.getProductById(id);
    }
    
    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return service.searchProducts(keyword);
    }
    
    @GetMapping("/searchProduct")
    public List<Product> searchproduct(@RequestParam String keyword) {
        return service.looseFuzzySearch(keyword);
    }

    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return service.getProductsByCategory(category);
    }
    
    @GetMapping("/paginated")
    public Page<Product> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        return service.getAllPaginated(page, size, sortBy);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody Product product) {
        return service.createProduct(product);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Product> createBatch(@Valid @RequestBody List<Product> products) {
        return service.createProducts(products);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteProduct(id);
    }
    
    //this is for cart access logic
    @PostMapping("/{id}/reserve")
    public ResponseEntity<?> reserveProduct(@PathVariable Long id, @RequestParam int quantity) {
        try {
            Product product = service.reserveProduct(id, quantity);
            return ResponseEntity.ok(product);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/{id}/CheckAvailability")
    public ResponseEntity<?> reserveProductCart(@PathVariable Long id, @RequestParam int quantity) {
        try {
            Product product = service.reserveProductCart(id, quantity);
            return ResponseEntity.ok(product);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    //increment prd qnty when decremented the quantity in cart
    @PostMapping("/{id}/release")
    public ResponseEntity<?> releaseProduct(@PathVariable Long id, @RequestParam int quantity) {
        try {
            Product product = service.releaseProduct(id, quantity);
            return ResponseEntity.ok(product);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    

}