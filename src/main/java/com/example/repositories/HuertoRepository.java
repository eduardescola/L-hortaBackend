package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Huerto;

public interface HuertoRepository extends JpaRepository<Huerto, Long> {}
