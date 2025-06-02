package com.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shopping_cart_items")
public class ShoppingCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference("cart-items")
    private ShoppingCart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_product_id", nullable = false)
    private GardenProduct gardenProduct;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity = BigDecimal.ONE;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();
}