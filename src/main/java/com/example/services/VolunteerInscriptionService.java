package com.example.services;

import com.example.entities.VolunteerInscription;
import com.example.repositories.VolunteerInscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerInscriptionService {

    @Autowired
    private VolunteerInscriptionRepository repository;

    public List<VolunteerInscription> findAll() {
        return repository.findAll();
    }

    public Optional<VolunteerInscription> findById(Long id) {
        return repository.findById(id);
    }

    public VolunteerInscription save(VolunteerInscription inscription) {
        return repository.save(inscription);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
