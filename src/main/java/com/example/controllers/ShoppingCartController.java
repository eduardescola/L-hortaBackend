package com.example.controllers;

import com.example.dto.ShoppingCartDTO;
import com.example.entities.User;
import com.example.repositories.UserRepository;
import com.example.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ShoppingCartDTO> getCart(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(user -> ResponseEntity.ok(shoppingCartService.getCart(user.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(
            Authentication authentication,
            @RequestParam Long gardenProductId,
            @RequestParam BigDecimal quantity) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(user -> {
                    shoppingCartService.addItem(user.getId(), gardenProductId, quantity);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/items/{gardenProductId}")
    public ResponseEntity<Void> updateItemQuantity(
            Authentication authentication,
            @PathVariable Long gardenProductId,
            @RequestParam BigDecimal quantity) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(user -> {
                    shoppingCartService.updateItemQuantity(user.getId(), gardenProductId, quantity);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/items/{gardenProductId}")
    public ResponseEntity<Void> removeItem(
            Authentication authentication,
            @PathVariable Long gardenProductId) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(user -> {
                    shoppingCartService.removeItem(user.getId(), gardenProductId);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(user -> {
                    shoppingCartService.clearCart(user.getId());
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
