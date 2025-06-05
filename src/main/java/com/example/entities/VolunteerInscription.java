package com.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "volunteer_session_inscriptions")
public class VolunteerInscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("inscription-user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonBackReference("session-inscriptions")
    private VolunteerSession session;

    @Column(name = "inscription_datetime", nullable = false, updatable = false)
    private LocalDateTime inscriptionDatetime;

    @PrePersist
    protected void onCreate() {
        inscriptionDatetime = LocalDateTime.now();
    }
}
