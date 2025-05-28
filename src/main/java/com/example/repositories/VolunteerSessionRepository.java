package com.example.repositories;

import com.example.entities.VolunteerSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerSessionRepository extends JpaRepository<VolunteerSession, Long> {
    List<VolunteerSession> findByGardenId(Long gardenId);
}
