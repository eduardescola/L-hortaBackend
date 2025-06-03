package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.GardenProduct;

public interface GardenProductRepository extends JpaRepository<GardenProduct, Long>{
    List<GardenProduct> findByGardenId(Long gardenId);
    List<GardenProduct> findByProductId(Long productId);
    GardenProduct findByGardenIdAndProductId(Long gardenId, Long productId);
}
