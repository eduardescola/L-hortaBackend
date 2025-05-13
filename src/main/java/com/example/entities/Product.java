package com.example.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "unit_price")
    private Double unitPrice;

    private int stock;

    @ManyToOne
    @JoinColumn(name = "garden_id")
    private Garden garden;
}
