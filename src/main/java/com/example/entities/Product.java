package com.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ca_name", length=100, nullable = false)
    private String caName;

    @Column(name="es_name", length=100, nullable = false)
    private String esName;

    @Column(name="en_name", length=100, nullable = false)
    private String enName;

    @Column(name="fr_name", length=100, nullable = false)
    private String frName;

    @Column(name="image", columnDefinition="TEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "garden_id")
//    @JsonBackReference("garden-products")
    @JsonIgnore
    private Garden garden;

    @OneToMany(mappedBy = "product")
    //@JsonManagedReference
    @JsonIgnore
    private List<GardenProduct> gardenProduct;

}
