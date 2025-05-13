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

    public List<Garden> getAll() {
        return gardenRepository.findAll();
    }

    public Garden create(Garden garden) {
        return gardenRepository.save(garden);
    }

    public Optional<Garden> getById(Long id) {
        return gardenRepository.findById(id);
    }

    public void delete(Long id) {
        gardenRepository.deleteById(id);
    }
}

