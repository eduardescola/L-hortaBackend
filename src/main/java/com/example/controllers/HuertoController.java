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

import com.example.entities.Huerto;
import com.example.services.HuertoService;

@RestController
@RequestMapping("/huertos")
public class HuertoController {

    @Autowired
    private HuertoService huertoService;

    @GetMapping
    public List<Huerto> listar() {
        return huertoService.obtenerTodos();
    }

    @PostMapping
    public Huerto crear(@RequestBody Huerto huerto) {
        return huertoService.crear(huerto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Huerto> obtenerPorId(@PathVariable Long id) {
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

