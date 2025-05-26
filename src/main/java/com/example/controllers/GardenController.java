package com.example.controllers;

import com.example.entities.Garden;
import com.example.entities.User;
import com.example.services.GardenService;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/gardens")
public class GardenController {

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Garden> list() {
        return gardenService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Garden> get(@PathVariable Long id) {
        return gardenService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createGarden(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("products") String productsJson,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Garden garden = gardenService.createGarden(
                    name, description, location, user.getId(), productsJson,
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
        gardenService.delete(id);
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
}
