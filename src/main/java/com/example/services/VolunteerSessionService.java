package com.example.services;

import com.example.entities.VolunteerInscription;
import com.example.entities.VolunteerSession;
import com.example.entities.User;
import com.example.repositories.VolunteerSessionRepository;
import com.example.repositories.VolunteerInscriptionRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerSessionService {

    @Autowired
    private VolunteerSessionRepository sessionRepository;

    @Autowired
    private VolunteerInscriptionRepository inscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<VolunteerSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<VolunteerSession> getSessionsByGardenId(Long gardenId) {
        return sessionRepository.findByGardenId(gardenId);
    }

    public Optional<VolunteerSession> getSession(Long id) {
        return sessionRepository.findById(id);
    }

    public VolunteerSession createSession(VolunteerSession session) {
        return sessionRepository.save(session);
    }

    public VolunteerSession updateSession(Long id, VolunteerSession session) {
        if (sessionRepository.existsById(id)) {
            session.setId(id);
            return sessionRepository.save(session);
        }
        return null;
    }

    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }

    public boolean isUserRegistered(Long userId, Long sessionId) {
        return inscriptionRepository.existsByUserIdAndSessionId(userId, sessionId);
    }

    public VolunteerInscription registerUser(Long userId, Long sessionId) {
        if (!isUserRegistered(userId, sessionId)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            VolunteerSession session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            VolunteerInscription inscription = new VolunteerInscription();
            inscription.setUser(user);
            inscription.setSession(session);
            return inscriptionRepository.save(inscription);
        }
        return null;
    }

    public void unregisterUser(Long userId, Long sessionId) {
        VolunteerInscription inscription = inscriptionRepository.findByUserIdAndSessionId(userId, sessionId);
        if (inscription != null) {
            inscriptionRepository.delete(inscription);
        }
    }
}
