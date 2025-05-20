package com.example.repositories;

import com.example.entities.VolunteerInscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerInscriptionRepository extends JpaRepository<VolunteerInscription, Long> {
}
