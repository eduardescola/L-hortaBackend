package com.example.controllers;

import com.example.dto.GardenDetailDTO;
import com.example.dto.GardenListDTO;
import com.example.dto.GardenProductDTO;
import com.example.entities.Garden;
import com.example.entities.User;
import com.example.services.GardenService;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.mappers.GardenMapper;

import java.io.IOException;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gardens")
public class GardenController {

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GardenMapper gardenMapper;

    @GetMapping
    public List<GardenListDTO> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String product
    ) {
        return gardenService.getGardensWithFilters(name, location, product)
                .stream()
                .map(garden -> {
                    GardenListDTO dto = new GardenListDTO();
                    dto.setId(garden.getId());
                    dto.setName(garden.getName());
                    dto.setDescription(garden.getDescription());
                    dto.setImage(garden.getImage());
                    dto.setLocation(garden.getLocation());
                    dto.setProductAvailable(garden.isProductAvailable());
                    dto.setSessionAvailable(garden.isSessionAvailable());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GardenDetailDTO> get(@PathVariable Long id) {
        return gardenService.getGardenDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createGarden(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("postalCode") String postalCode,
            @RequestParam("products") String productsJson,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication) {
        System.out.println("HOli");
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println(user.getId());

            Garden garden = gardenService.createGarden(
                    name, description, location, postalCode, user.getId(), productsJson,
                    image
            );
            return ResponseEntity.ok(garden);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error creating garden: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gardenService.deleteGarden(id);
        return ResponseEntity.noContent().build();
    }

    // 🔍 Buscar jardines por nombre exacto
    @GetMapping("/name/{name}")
    public List<Garden> findByName(@PathVariable String name) {
        return gardenService.findByName(name);
    }

    // 🔍 Buscar jardines por producto
    @GetMapping("/products/{product}")
    public List<Garden> findByProduct(@PathVariable String product) {
        return gardenService.findByProduct(product);
    }

    // 🔍 Buscar jardines por ubicación del dueño
    @GetMapping("/location/{location}")
    public List<Garden> findByLocation(@PathVariable String location) {
        return gardenService.findByLocation(location);
    }

    @GetMapping("/{gardenId}/products")
    public List<GardenProductDTO> getGardenProducts(@PathVariable Long gardenId) {
        return gardenService.getGardenProducts(gardenId);
    }

    @GetMapping("/my-gardens")
    public List<GardenDetailDTO> getMyGardens(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(user -> gardenService.getGardensForListByUserId(user.getId()))
                .orElse(Collections.emptyList());
    }

}
