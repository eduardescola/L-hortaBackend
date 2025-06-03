package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entities.User;
import com.example.security.JwtUtil;
import com.example.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

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
        System.out.println("Holi");
        return ResponseEntity.notFound().build();
//        return userService.obtenerPorId(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
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

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestPart("user") String userJson,  // JSON como String
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest request) {

        try {
            // Obtener el token del header Authorization
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Token no proporcionado.");
            }

            String token = authHeader.substring(7); // quitar "Bearer "
            Long userIdFromToken = jwtUtil.extractUserId(token); // Método en JwtUtil

            if (!id.equals(userIdFromToken)) {
                return ResponseEntity.status(403).body("No tienes permiso para modificar este perfil.");
            }

            // Convertir JSON String a objeto User
            ObjectMapper mapper = new ObjectMapper();
            User updates = mapper.readValue(userJson, User.class);

            // Lógica para guardar la imagen si viene
            if (profileImage != null && !profileImage.isEmpty()) {
                // Por ejemplo, guardar la imagen y actualizar la ruta en updates
                String imagePath = userService.saveProfileImage(id, profileImage);
                updates.setProfileImage(imagePath);
            }

            boolean actualizado = userService.updateProfile(id, updates);
            if (actualizado) {
                return ResponseEntity.ok("Perfil actualizado correctamente");
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
