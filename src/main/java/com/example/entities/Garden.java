package com.example.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gardens")
public class Garden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=100)
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition="TEXT")
    private String image;

    @Column(length=255)
    private String location;

    @Column(name="postal_code", length=25)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-gardens")
    private User user;
    
    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("garden-products")
    private List<GardenProduct> gardenProducts;

    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("garden-sessions")
    private List<VolunteerSession> sessions;

    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("garden-orders")
    private List<Order> orders;

    @JsonProperty("productAvailable")
    public boolean isProductAvailable() {
        return gardenProducts != null && !gardenProducts.isEmpty();
    }

    @JsonProperty("volunteerSessionAvailable")
    public boolean isSessionAvailable() {
        return sessions != null && !sessions.isEmpty();
    }
}
