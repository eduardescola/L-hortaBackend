package com.example.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.entities.User;
import com.example.repositories.UserRepository;
import com.example.security.JwtUtil;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173") // Ajusta según tu frontend
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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
        public String location;
    }

    // ✅ Token Response
    static class TokenResponse {
        public String token;

        public TokenResponse(String token) {
            this.token = token;
        }
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user);
        return new TokenResponse(token);
    }

    @PostMapping("/register")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.email);
        if (userOpt.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        User newUser = new User();
        newUser.setName(request.name);
        newUser.setSurname(request.surname);
        newUser.setEmail(request.email);
        newUser.setPassword(passwordEncoder.encode(request.password));
        newUser.setLocation(request.location);
        newUser.setRole("USER");

        userRepository.save(newUser);

        String token = jwtUtil.generateToken(newUser);
        return new TokenResponse(token);
    }
}
