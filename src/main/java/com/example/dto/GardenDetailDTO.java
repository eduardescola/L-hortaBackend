package com.example.dto;

import lombok.Data;
import java.util.List;

@Data
public class GardenDetailDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String location;
    private String postalCode;
    private List<GardenProductDTO> gardenProducts;
    private boolean productAvailable;
    private boolean sessionAvailable;
    private Long userId;
}