package com.example.repositories;

import com.example.entities.VolunteerSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerSessionRepository extends JpaRepository<VolunteerSession, Long> {
}
