package com.example.services;

import com.example.entities.GardenProduct;
import com.example.repositories.GardenProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GardenProductService {

    @Autowired
    private GardenProductRepository gardenProductRepository;

    public List<GardenProduct> getAllGardenProducts() {
        return gardenProductRepository.findAll();
    }

    public List<GardenProduct> getGardenProductsByGardenId(Long gardenId) {
        return gardenProductRepository.findByGardenId(gardenId);
    }

    public List<GardenProduct> getGardenProductsByProductId(Long productId) {
        return gardenProductRepository.findByProductId(productId);
    }

    public Optional<GardenProduct> getGardenProduct(Long id) {
        return gardenProductRepository.findById(id);
    }

    public GardenProduct createGardenProduct(GardenProduct gardenProduct) {
        return gardenProductRepository.save(gardenProduct);
    }

    public GardenProduct updateGardenProduct(Long id, GardenProduct gardenProduct) {
        if (gardenProductRepository.existsById(id)) {
            gardenProduct.setId(id);
            return gardenProductRepository.save(gardenProduct);
        }
        return null;
    }

    public void deleteGardenProduct(Long id) {
        gardenProductRepository.deleteById(id);
    }

    public GardenProduct findByGardenAndProduct(Long gardenId, Long productId) {
        return gardenProductRepository.findByGardenIdAndProductId(gardenId, productId);
    }
}