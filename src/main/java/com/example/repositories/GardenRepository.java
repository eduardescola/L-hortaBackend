package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Garden;

public interface GardenRepository extends JpaRepository<Garden, Long> {}
