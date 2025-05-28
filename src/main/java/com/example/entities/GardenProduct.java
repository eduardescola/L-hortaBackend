package com.example.entities;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "garden_products")
public class GardenProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="garden_id", nullable = false)
    @JsonBackReference(value = "garden-gardenProducts")
    private Garden garden;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    @JsonBackReference(value = "product-gardenProducts")
    private Product product;


    @Column(name="unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(precision = 10, scale = 3)
    private BigDecimal stock;

    @Column(length = 25)
    private String units;
}
