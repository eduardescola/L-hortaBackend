package com.example.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Garden;
import com.example.repositories.GardenRepository;

@Service
public class GardenService {

    @Autowired
    private GardenRepository gardenRepository;

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

    // 🔍 Buscar por nombre exacto del jardín
    public List<Garden> findByName(String name) {
        return gardenRepository.findByName(name);
    }

    public List<Garden> findByProduct(String product) {
        return gardenRepository.findByProduct(product);
    }

    // 🔍 Buscar por ubicación del usuario propietario
    public List<Garden> findByLocation(String location) {
        return gardenRepository.findByLocation(location);
    }
}
