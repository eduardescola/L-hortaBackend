package com.example.controllers;

import com.example.entities.VolunteerInscription;
import com.example.services.VolunteerInscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer-inscriptions")
public class VolunteerInscriptionController {

    @Autowired
    private VolunteerInscriptionService service;

    @GetMapping
    public List<VolunteerInscription> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolunteerInscription> getById(@PathVariable Long id) {
        Optional<VolunteerInscription> result = service.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public VolunteerInscription create(@RequestBody VolunteerInscription inscription) {
        return service.save(inscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
