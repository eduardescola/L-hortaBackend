package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entities.Garden;

public interface GardenRepository extends JpaRepository<Garden, Long> {

    List<Garden> findByUserId(Long userId);

    // Buscar jardines por nombre exacto
    List<Garden> findByName(String name);

    // Buscar jardines por ubicación del usuario propietario
    List<Garden> findByLocation(String location);

    @Query("SELECT DISTINCT g FROM Garden g JOIN g.gardenProducts gp JOIN gp.product p WHERE p.caName LIKE %:productName% OR p.esName LIKE %:productName% OR p.enName LIKE %:productName%")
    List<Garden> findByProduct(@Param("productName") String productName);
}
