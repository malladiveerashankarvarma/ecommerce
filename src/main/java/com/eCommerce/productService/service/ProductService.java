package com.eCommerce.productService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eCommerce.productService.entity.Product;
import com.eCommerce.productService.exception.ResourceNotFoundException;
import com.eCommerce.productService.repository.ProductRepository;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }
    
    public List<Product> searchProducts(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return repository.findByCategory(category);
    }


    public Product createProduct(Product product) {
    	if (repository.findByName(product.getName()).isPresent()) {
    	    throw new IllegalArgumentException("Product already exists");
    	}
        return repository.save(product);
    }
    
    public Page<Product> getAllPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable);
    }
    
    //FUZZY SEARCH LOOSE
    public String buildLoosePattern(String keyword) {
        return "%" + keyword.chars()
            .mapToObj(c -> String.valueOf((char)c))
            .collect(Collectors.joining("%")) + "%";
    }
    
    public List<Product> looseFuzzySearch(String keyword) {
        String pattern = buildLoosePattern(keyword);
        return repository.fuzzyLooseMatch(pattern);
    }
    

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setImageURL(updatedProduct.getImageURL());
        return repository.save(existing);
    }
    
    
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

	public List<Product> createProducts(@Valid List<Product> products) {
		// TODO Auto-generated method stub
		return repository.saveAll(products);
	}
}