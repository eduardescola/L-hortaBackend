package com.example.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=100)
    private String name;

    @Column(length=100)
    private String surname;

    @Column(length=255, unique = true)
    private String email; // Campo email agregado

    @Column(length=255)
    private String location; // <-- Nuevo campo de ubicación

    @JsonIgnore
    @Column(length=255)
    private String password;

    @Column(length=50)
    private String role;
    
    @Column(length = 255)
    private String profileImage; // <-- Ruta o URL de la imagen de perfil

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-gardens")
    private List<Garden> gardens;
}
