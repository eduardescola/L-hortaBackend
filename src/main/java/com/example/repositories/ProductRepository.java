package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCaNameContaining(String name);
    List<Product> findByEsNameContaining(String name);
    List<Product> findByEnNameContaining(String name);
    List<Product> findByGardenId(Long gardenId);
}
