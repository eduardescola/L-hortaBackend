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

    @Query("""
    	    SELECT DISTINCT g FROM Garden g
    	    LEFT JOIN g.gardenProducts gp
    	    LEFT JOIN gp.product p
    	    WHERE (:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%')))
    	    AND (:location IS NULL OR LOWER(g.location) LIKE LOWER(CONCAT('%', :location, '%')))
    	    AND (
    	        :productName IS NULL OR (
    	            (:lang = 'es' AND LOWER(p.esName) LIKE LOWER(CONCAT('%', :productName, '%')))
    	            OR (:lang = 'en' AND LOWER(p.enName) LIKE LOWER(CONCAT('%', :productName, '%')))
    	            OR (:lang = 'fr' AND LOWER(p.frName) LIKE LOWER(CONCAT('%', :productName, '%')))
    	            OR (:lang = 'ca' AND LOWER(p.caName) LIKE LOWER(CONCAT('%', :productName, '%')))
    	        )
    	    )
    	""")
    List<Garden> filterGardens(
            @Param("name") String name,
            @Param("location") String location,
            @Param("productName") String productName,
            @Param("lang") String lang
    );
}
