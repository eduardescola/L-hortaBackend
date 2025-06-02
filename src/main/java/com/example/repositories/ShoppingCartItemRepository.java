package com.example.repositories;

import com.example.entities.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    Optional<ShoppingCartItem> findByCartIdAndGardenProductId(Long cartId, Long gardenProductId);
    void deleteByCartId(Long cartId);
}