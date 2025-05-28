package com.example.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garden_id")
    @JsonBackReference("garden-orders")
    private Garden garden;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-orders")
    private User user;

    private LocalDateTime date;

    @Column(length = 50)
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference("order-items")
    private List<OrderItem> items;
}
