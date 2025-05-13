package com.example.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gardens")
public class Garden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String image;
    private String location;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Asegúrate de tener la clase User.java como entidad
}
