package com.example.controllers;

import com.example.entities.VolunteerSession;
import com.example.services.VolunteerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class VolunteerSessionController {

    @Autowired
    private VolunteerSessionService sessionService;

    @GetMapping
    public List<VolunteerSession> list() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/garden/{gardenId}")
    public List<VolunteerSession> getSessionsByGarden(@PathVariable Long gardenId) {
        return sessionService.getSessionsByGardenId(gardenId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolunteerSession> get(@PathVariable Long id) {
        return sessionService.getSession(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public VolunteerSession create(@RequestBody VolunteerSession session) {
        return sessionService.createSession(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
