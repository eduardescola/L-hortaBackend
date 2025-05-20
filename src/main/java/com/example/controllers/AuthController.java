package com.example.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.entities.User;
import com.example.repositories.UserRepository;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173") // Permitir CORS desde frontend
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Login DTO
    static class LoginRequest {
        public String email;
        public String password;
    }

    // Register DTO
    static class RegisterRequest {
        public String name;
        public String surname;
        public String email;
        public String password;
    }

    // Response DTO
    static class LoginResponse {
        public String message;

        public LoginResponse(String message) {
            this.message = message;
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = userOpt.get();
        boolean matches = passwordEncoder.matches(request.password, user.getPassword());

        if (!matches) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new LoginResponse("Login correcto");
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.email);
        if (userOpt.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        User newUser = new User();
        newUser.setName(request.name);
        newUser.setSurname(request.surname);
        newUser.setEmail(request.email);
        newUser.setPassword(passwordEncoder.encode(request.password));
        newUser.setRole("USER"); // Por defecto

        userRepository.save(newUser);

        return new LoginResponse("Usuario registrado con éxito");
    }
}
