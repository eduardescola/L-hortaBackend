package com.example.controllers;

import com.example.entities.VolunteerSession;
import com.example.services.VolunteerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class VolunteerSessionController {

    @Autowired
    private VolunteerSessionService sessionService;

    @GetMapping
    public List<VolunteerSession> list() {
        return sessionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolunteerSession> get(@PathVariable Long id) {
        return sessionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public VolunteerSession create(@RequestBody VolunteerSession session) {
        return sessionService.save(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
