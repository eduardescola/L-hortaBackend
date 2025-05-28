package com.example.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entities.Garden;
import com.example.repositories.GardenRepository;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.example.entities.User;
import com.example.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class GardenService {

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<Garden> findAll() {
        return gardenRepository.findAll();
    }

    public Optional<Garden> findById(Long id) {
        return gardenRepository.findById(id);
    }

    public Garden save(Garden garden) {
        return gardenRepository.save(garden);
    }

    public void delete(Long id) {
        gardenRepository.deleteById(id);
    }

    // Buscar por nombre exacto del jardín
    public List<Garden> findByName(String name) {
        return gardenRepository.findByName(name);
    }

    public List<Garden> findByProduct(String product) {
        return gardenRepository.findByProduct(product);
    }

    // Buscar por ubicación del usuario propietario
    public List<Garden> findByLocation(String location) {
        return gardenRepository.findByLocation(location);
    }
    
    public Page<Garden> findAllPaged(Pageable pageable) {
        return gardenRepository.findAll(pageable);
    }

    public List<Garden> findByUserId(Long userId) {
        return gardenRepository.findByUserId(userId);
    }

    public Garden createGarden(
            String name,
            String description,
            String location,
            Long userId,
            String productsJson,
            MultipartFile image) throws IOException {

        // Create and populate garden entity
        Garden garden = new Garden();
        garden.setName(name);
        garden.setDescription(description);
        garden.setLocation(location);

        // Set user
        User user = new User();
        user.setId(userId);
        garden.setUser(user);

        // Parse and set products
        ObjectMapper mapper = new ObjectMapper();
        List<Product> products = mapper.readValue(productsJson,
                mapper.getTypeFactory().constructCollectionType(List.class, Product.class));
        garden.setProducts(products);


        // Handle image upload
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeFile(image, "gardens/");
            garden.setImage(imagePath);
        }

        return gardenRepository.save(garden);
    }
}
