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
    private GardenRepository huertoRepository;

    public List<Garden> obtenerTodos() {
        return huertoRepository.findAll();
    }

    public Garden crear(Garden huerto) {
        return huertoRepository.save(huerto);
    }

    public Optional<Garden> obtenerPorId(Long id) {
        return huertoRepository.findById(id);
    }

    public void eliminar(Long id) {
        huertoRepository.deleteById(id);
    }
}

