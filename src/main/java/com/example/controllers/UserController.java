package com.example.controllers;

import com.example.entities.User;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> listar() {
        return userService.obtenerTodos();
    }

    @PostMapping
    public User crear(@RequestBody User user) {
        return userService.crear(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> obtenerPorId(@PathVariable Long id) {
        return userService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        userService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/role")
    public ResponseEntity<?> cambiarRolAOwner(@PathVariable Long id) {
        boolean actualizado = userService.cambiarRolAOwner(id);
        if (actualizado) {
            return ResponseEntity.ok("Rol actualizado a OWNER");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //@PatchMapping("/{id}")
//    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
//        userService.updateProfile(id, updates);
//        return ResponseEntity.ok("Perfil actualitzat");
//    }

}
