package com.example.services;

import com.example.entities.User;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> obtenerTodos() {
        return userRepository.findAll();
    }

    public User crear(User user) {
        return userRepository.save(user);
    }

    public Optional<User> obtenerPorId(Long id) {
        return userRepository.findById(id);
    }

    public void eliminar(Long id) {
        userRepository.deleteById(id);
    }
}
