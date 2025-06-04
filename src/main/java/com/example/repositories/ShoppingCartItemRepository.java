package com.example.repositories;

import com.example.entities.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    Optional<ShoppingCartItem> findByCartIdAndGardenProductId(Long cartId, Long gardenProductId);
    void deleteByCartId(Long cartId);

    @Modifying
    @Query(value = "DELETE FROM shopping_cart_items WHERE cart_id = :cartId", nativeQuery = true)
    void deleteAllByCartId(@Param("cartId") Long cartId);
}