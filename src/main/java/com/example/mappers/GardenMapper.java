package com.example.mappers;

import com.example.dto.GardenListDTO;
import com.example.entities.Garden;
import org.springframework.stereotype.Component;

@Component
public class GardenMapper {

    public GardenListDTO toHomeDTO(Garden garden) {
        if (garden == null) {
            return null;
        }

        GardenListDTO dto = new GardenListDTO();
        dto.setId(garden.getId());
        dto.setName(garden.getName());
        dto.setDescription(garden.getDescription());
        dto.setImage(garden.getImage());
        dto.setLocation(garden.getLocation());
        dto.setPostalCode(garden.getPostalCode());
        dto.setProductAvailable(garden.isProductAvailable());
        dto.setSessionAvailable(garden.isSessionAvailable());

        return dto;
    }
}