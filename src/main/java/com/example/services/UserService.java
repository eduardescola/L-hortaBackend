package com.example.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entities.User;
import com.example.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private FileStorageService fileStorageService;

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

    public boolean updateProfile(Long id, User updates) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (updates.getName() != null) {
                existingUser.setName(updates.getName());
            }
            if (updates.getSurname() != null) {
                existingUser.setSurname(updates.getSurname());
            }
            if (updates.getEmail() != null) {
                existingUser.setEmail(updates.getEmail());
            }
            if (updates.getLocation() != null) {
                existingUser.setLocation(updates.getLocation());
            }
            if (updates.getPassword() != null && !updates.getPassword().isBlank()) {
                String cifrada = passwordEncoder.encode(updates.getPassword());
                existingUser.setPassword(cifrada);
            }
            if (updates.getProfileImage() != null && !updates.getProfileImage().isBlank()) {
                existingUser.setProfileImage(updates.getProfileImage());
            }

            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    public String saveProfileImage(Long userId, MultipartFile imageFile) {
        Optional<User> userOpt = obtenerPorId(userId);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();

        try {
            // Guarda la imagen en una carpeta 'users/' usando FileStorageService
            String imagePath = fileStorageService.storeFile(imageFile, "users/");
            user.setProfileImage(imagePath);  // Asumiendo que User tiene un campo profileImage (String)
            userRepository.save(user);
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
