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
    public ResponseEntity<?> createFromCart(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            return userRepository.findByEmail(userEmail)
                    .map(user -> {
                        try {
                            List<OrderDTO> orders = orderService.createOrdersFromCart(user.getId());
                            return ResponseEntity.ok(orders);
                        } catch (RuntimeException e) {
                            return ResponseEntity.badRequest().body(e.getMessage());
                        }
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> list(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            List<OrderDTO> orders = userRepository.findByEmail(userEmail)
                    .map(user -> orderService.findByUserId(user.getId()))
                    .orElse(Collections.emptyList());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> get(@PathVariable Long id, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            return userRepository.findByEmail(userEmail)
                    .map(user -> orderService.findById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build()))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            return userRepository.findByEmail(userEmail)
                    .map(user -> {
                        orderService.delete(id);
                        return ResponseEntity.noContent().<Void>build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
