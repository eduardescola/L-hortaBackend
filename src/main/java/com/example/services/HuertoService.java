package com.example.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Huerto;
import com.example.repositories.HuertoRepository;

@Service
public class HuertoService {

    @Autowired
    private HuertoRepository huertoRepository;

    public List<Huerto> obtenerTodos() {
        return huertoRepository.findAll();
    }

    public Huerto crear(Huerto huerto) {
        return huertoRepository.save(huerto);
    }

    public Optional<Huerto> obtenerPorId(Long id) {
        return huertoRepository.findById(id);
    }

    public void eliminar(Long id) {
        huertoRepository.deleteById(id);
    }
}

