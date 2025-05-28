package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entities.Garden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GardenRepository extends JpaRepository<Garden, Long> {
    List<Garden> findByName(String name);
    List<Garden> findByLocation(String location);

    @Query("SELECT DISTINCT g FROM Garden g JOIN g.products p WHERE p.name = :productName")
    List<Garden> findByProduct(@Param("productName") String productName);

    // 🔄 Añadir paginación
    Page<Garden> findAll(Pageable pageable);
    
    List<Garden> findByUserId(Long userId); // ✅ Este método
    
}

