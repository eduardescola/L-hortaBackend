package com.example.services;

import com.example.entities.Garden;
import com.example.repositories.GardenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
