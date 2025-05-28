package com.example.repositories;

import com.example.entities.VolunteerInscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerInscriptionRepository extends JpaRepository<VolunteerInscription, Long> {
    List<VolunteerInscription> findByUserId(Long userId);
    List<VolunteerInscription> findBySessionId(Long sessionId);
    VolunteerInscription findByUserIdAndSessionId(Long userId, Long sessionId);
    boolean existsByUserIdAndSessionId(Long userId, Long sessionId);
}
