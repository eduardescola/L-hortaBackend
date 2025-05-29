package com.example.services;

import java.util.List;
import java.util.Optional;

import com.example.dto.GardenDetailDTO;
import com.example.dto.GardenProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entities.Garden;
import com.example.repositories.GardenRepository;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.example.entities.User;
import com.example.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.entities.GardenProduct;
import com.example.repositories.GardenProductRepository;
import com.example.repositories.ProductRepository;
import com.example.dto.GardenListDTO;
import java.util.stream.Collectors;

@Service
public class GardenService {

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GardenProductRepository gardenProductRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Garden> getAllGardens() {
        return gardenRepository.findAll();
    }

    public Optional<Garden> getGardenById(Long id) {
        return gardenRepository.findById(id);
    }

    public List<Garden> getGardensByUserId(Long userId) {
        return gardenRepository.findByUserId(userId);
    }

    public Garden createGarden(
            String name,
            String description,
            String location,
            String postalCode,
            Long userId,
            String productsJson,
            MultipartFile image) throws IOException {

        // Create and populate garden entity
        Garden garden = new Garden();
        garden.setName(name);
        garden.setDescription(description);
        garden.setLocation(location);
        garden.setPostalCode(postalCode);

        // Set user
        User user = new User();
        user.setId(userId);
        garden.setUser(user);


        // Handle image upload
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeFile(image, "gardens/");
            garden.setImage(imagePath);
        }

        garden = gardenRepository.save(garden);

        // Parse and set products
        if (productsJson != null && !productsJson.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            List<GardenProduct> gardenProducts = mapper.readValue(productsJson,
                    mapper.getTypeFactory().constructCollectionType(List.class, GardenProduct.class));

            for (GardenProduct gardenProduct : gardenProducts) {
                gardenProduct.setGarden(garden);
                gardenProductRepository.save(gardenProduct);
            }
        }

        return gardenRepository.save(garden);
    }

    public Garden updateGarden(Long id, Garden garden) {
        if (gardenRepository.existsById(id)) {
            garden.setId(id);
            return gardenRepository.save(garden);
        }
        return null;
    }

    public void deleteGarden(Long id) {
        gardenRepository.deleteById(id);
    }

    public GardenProduct addProductToGarden(Long gardenId, Long productId, GardenProduct gardenProduct) {
        Garden garden = gardenRepository.findById(gardenId)
                .orElseThrow(() -> new RuntimeException("Garden not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        gardenProduct.setGarden(garden);
        gardenProduct.setProduct(product);
        return gardenProductRepository.save(gardenProduct);
    }

    public GardenProduct updateGardenProduct(Long gardenId, Long productId, GardenProduct gardenProduct) {
        GardenProduct existingProduct = gardenProductRepository.findByGardenIdAndProductId(gardenId, productId);
        if (existingProduct != null) {
            Garden garden = gardenRepository.findById(gardenId)
                    .orElseThrow(() -> new RuntimeException("Garden not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            gardenProduct.setId(existingProduct.getId());
            gardenProduct.setGarden(garden);
            gardenProduct.setProduct(product);
            return gardenProductRepository.save(gardenProduct);
        }
        return null;
    }

    public void removeProductFromGarden(Long gardenId, Long productId) {
        GardenProduct gardenProduct = gardenProductRepository.findByGardenIdAndProductId(gardenId, productId);
        if (gardenProduct != null) {
            gardenProductRepository.delete(gardenProduct);
        }
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

    public List<GardenListDTO> getAllGardensForList() {
        return gardenRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GardenListDTO convertToDTO(Garden garden) {
        GardenListDTO dto = new GardenListDTO();
        dto.setId(garden.getId());
        dto.setName(garden.getName());
        dto.setDescription(garden.getDescription());
        dto.setImage(garden.getImage());
        dto.setLocation(garden.getLocation());
        dto.setProductAvailable(garden.isProductAvailable());
        dto.setSessionAvailable(garden.isSessionAvailable());
        return dto;
    }

    public List<GardenProductDTO> getGardenProducts(Long gardenId) {
        return gardenProductRepository.findByGardenId(gardenId).stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());
    }

    public List<Garden> findByUserId(Long userId) {
        return gardenRepository.findByUserId(userId);
    }

    private GardenProductDTO convertToProductDTO(GardenProduct gardenProduct) {
        GardenProductDTO dto = new GardenProductDTO();
        dto.setId(gardenProduct.getId());
        dto.setCaName(gardenProduct.getProduct().getCaName());
        dto.setEsName(gardenProduct.getProduct().getEsName());
        dto.setEnName(gardenProduct.getProduct().getEnName());
        dto.setStock(gardenProduct.getStock());
        dto.setUnitPrice(gardenProduct.getUnitPrice());
        dto.setUnits(gardenProduct.getUnits());
        return dto;
    }

    public Optional<GardenDetailDTO> getGardenDetail(Long id) {
        return gardenRepository.findById(id)
                .map(this::convertToDetailDTO);
    }

    public List<GardenDetailDTO> getGardensForListByUserId(Long userId) {
        return gardenRepository.findByUserId(userId).stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
    }

    private GardenDetailDTO convertToDetailDTO(Garden garden) {
        GardenDetailDTO dto = new GardenDetailDTO();
        dto.setId(garden.getId());
        dto.setName(garden.getName());
        dto.setDescription(garden.getDescription());
        dto.setImage(garden.getImage());
        dto.setLocation(garden.getLocation());
        dto.setPostalCode(garden.getPostalCode());
        dto.setProductAvailable(garden.isProductAvailable());
        dto.setSessionAvailable(garden.isSessionAvailable());

        // Convert garden products to DTOs
        List<GardenProductDTO> productDTOs = garden.getGardenProducts().stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());
        dto.setGardenProducts(productDTOs);

        return dto;
    }
    public List<Garden> getGardensWithFilters(String name, String location, String productName) {
        if (name != null && !name.isEmpty()) {
        	return gardenRepository.findByNameLike(name);

        } else if (location != null && !location.isEmpty()) {
            return gardenRepository.findByLocation(location);
        } else if (productName != null && !productName.isEmpty()) {
            return gardenRepository.findByProduct(productName);
        } else {
            return gardenRepository.findAll();
        }
    }



}
