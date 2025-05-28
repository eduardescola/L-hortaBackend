package com.example.repositories;

import com.example.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCaNameContaining(String name);
    List<Product> findByEsNameContaining(String name);
    List<Product> findByEnNameContaining(String name);
    List<Product> findByGardenId(Long gardenId);
}
