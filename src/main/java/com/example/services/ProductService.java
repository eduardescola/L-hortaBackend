package com.example.services;

import com.example.entities.Product;
import com.example.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
    
    public Page<Product> findAllPaged(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findByGardenIdPaged(Long gardenId, Pageable pageable) {
        return productRepository.findByGardenId(gardenId, pageable);
    }
}
