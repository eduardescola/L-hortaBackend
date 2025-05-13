package com.example.services;

import com.example.entities.VolunteerSession;
import com.example.repositories.VolunteerSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerSessionService {

    @Autowired
    private VolunteerSessionRepository sessionRepository;

    public List<VolunteerSession> findAll() {
        return sessionRepository.findAll();
    }

    public Optional<VolunteerSession> findById(Long id) {
        return sessionRepository.findById(id);
    }

    public VolunteerSession save(VolunteerSession session) {
        return sessionRepository.save(session);
    }

    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }
}
