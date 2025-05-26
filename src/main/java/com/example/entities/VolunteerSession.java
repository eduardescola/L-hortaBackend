package com.example.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "volunteer_sessions")
public class VolunteerSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garden_id")
    @JsonBackReference
    private Garden garden;

    private LocalDateTime datetime;

    @Column(name = "max_volunteers")
    private int maxVolunteers;

    @Column(name = "task_description")
    private String taskDescription;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VolunteerInscription> inscriptions;

    @Transient
    public int getInscriptions() {
        return maxVolunteers;
    }

    @Transient
    public int getAvailableSpots() {
        return maxVolunteers - inscriptions.size();
    }
}
