package com.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference("order-items")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference("products-item")
    private Product product;

    private int quantity;

    @Column(name="unit_price", precision=10, scale=2)
    private BigDecimal unitPrice; // Important per si canvia el preu d'un producte més endavant
}
