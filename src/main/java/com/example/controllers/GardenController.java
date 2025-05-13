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
@RequestMapping("/huertos")
public class GardenController {

    @Autowired
    private GardenService huertoService;

    @GetMapping
    public List<Garden> listar() {
        return huertoService.obtenerTodos();
    }

    @PostMapping
    public Garden crear(@RequestBody Garden huerto) {
        return huertoService.crear(huerto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Garden> obtenerPorId(@PathVariable Long id) {
        return huertoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        huertoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

