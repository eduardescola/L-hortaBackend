package com.example.controllers;

import com.example.dto.OrderDTO;
import com.example.repositories.UserRepository;
import com.example.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create-from-cart")
    public ResponseEntity<OrderDTO> createFromCart(Authentication authentication) {
        try {
            Long userId = Long.parseLong(authentication.getName());
            OrderDTO order = orderService.createOrderFromCart(userId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<OrderDTO> list(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(user -> orderService.findByUserId(user.getId()))
                .orElse(Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> get(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = Long.parseLong(authentication.getName());
            return orderService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = Long.parseLong(authentication.getName());
            orderService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
