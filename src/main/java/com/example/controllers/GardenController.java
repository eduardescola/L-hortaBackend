package com.example.controllers;

import com.example.entities.Garden;
import com.example.services.GardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gardens")
public class GardenController {

    @Autowired
    private GardenService gardenService;

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

    @PostMapping
    public Garden create(@RequestBody Garden garden) {
        return gardenService.save(garden);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gardenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
