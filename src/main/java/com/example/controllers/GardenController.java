package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Garden;
import com.example.services.GardenService;

@RestController
@RequestMapping("/gardens")
public class GardenController {

    @Autowired
    private GardenService huertoService;

    @GetMapping
    public List<Garden> getAllGardens() {
        return huertoService.getAll();
    }

    @PostMapping
    public Garden createGarden(@RequestBody Garden garden) {
        return huertoService.create(garden);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Garden> getById(@PathVariable Long id) {
        return huertoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        huertoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

