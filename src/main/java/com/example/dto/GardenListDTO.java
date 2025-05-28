package com.example.dto;

import lombok.Data;

@Data
public class GardenListDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String location;
    private String postalCode;
    private boolean productAvailable;
    private boolean sessionAvailable;
}