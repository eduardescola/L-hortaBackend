package com.example.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entities.User;
import com.example.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> obtenerTodos() {
        return userRepository.findAll();
    }

    public User crear(User user) {
        String passwordCifrada = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordCifrada);
        return userRepository.save(user);
    }

    public Optional<User> obtenerPorId(Long id) {
        return userRepository.findById(id);
    }

    public void eliminar(Long id) {
        userRepository.deleteById(id);
    }
    
    public boolean cambiarRolAOwner(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole("OWNER");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void updateProfile(){

    }
}
