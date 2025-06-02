package com.example.services;

import com.example.dto.ShoppingCartDTO;
import com.example.entities.GardenProduct;
import com.example.entities.ShoppingCart;
import com.example.entities.ShoppingCartItem;
import com.example.entities.User;
import com.example.mappers.ShoppingCartMapper;
import com.example.repositories.GardenProductRepository;
import com.example.repositories.ShoppingCartItemRepository;
import com.example.repositories.ShoppingCartRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartItemRepository cartItemRepository;

    @Autowired
    private GardenProductRepository gardenProductRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Transactional
    public ShoppingCart getOrCreateCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user);
                    return shoppingCartRepository.save(cart);
                });
    }

    @Transactional
    public void addItem(Long userId, Long gardenProductId, BigDecimal quantity) {
        ShoppingCart cart = getOrCreateCart(userId);
        GardenProduct gardenProduct = gardenProductRepository.findById(gardenProductId)
                .orElseThrow(() -> new RuntimeException("Garden product not found"));

        Optional<ShoppingCartItem> existingItem = cartItemRepository
                .findByCartIdAndGardenProductId(cart.getId(), gardenProductId);

        if (existingItem.isPresent()) {
            ShoppingCartItem item = existingItem.get();
            item.setQuantity(item.getQuantity().add(quantity));
            cartItemRepository.save(item);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setCart(cart);
            newItem.setGardenProduct(gardenProduct);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public void updateItemQuantity(Long userId, Long gardenProductId, BigDecimal quantity) {
        ShoppingCart cart = getOrCreateCart(userId);
        cartItemRepository.findByCartIdAndGardenProductId(cart.getId(), gardenProductId)
                .ifPresent(item -> {
                    if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                        cartItemRepository.delete(item);
                    } else {
                        item.setQuantity(quantity);
                        cartItemRepository.save(item);
                    }
                });
    }

    @Transactional
    public void removeItem(Long userId, Long gardenProductId) {
        ShoppingCart cart = getOrCreateCart(userId);
        cartItemRepository.findByCartIdAndGardenProductId(cart.getId(), gardenProductId)
                .ifPresent(cartItemRepository::delete);
    }

    @Transactional
    public void clearCart(Long userId) {
        ShoppingCart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartId(cart.getId());
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getCart(Long userId) {
        ShoppingCart cart = getOrCreateCart(userId);
        return shoppingCartMapper.toDTO(cart);
    }
}
