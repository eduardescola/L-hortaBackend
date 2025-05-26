package com.example.repositories;

import com.example.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByGardenId(Long gardenId);

    // 🔄 Añadir paginación
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByGardenId(Long gardenId, Pageable pageable);
}
