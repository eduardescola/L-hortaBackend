package com.example.repositories;

import com.example.entities.GardenProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface GardenProductRepository extends JpaRepository<GardenProduct, Long>{
    List<GardenProduct> findByGardenId(Long gardenId);
    List<GardenProduct> findByProductId(Long productId);
    GardenProduct findByGardenIdAndProductId(Long gardenId, Long productId);
}
