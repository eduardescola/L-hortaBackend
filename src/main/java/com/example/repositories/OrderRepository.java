package com.example.repositories;

import com.example.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // <- Esto ayuda a evitar errores en algunos contextos
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);  // <- Este método permite obtener pedidos por usuario
}
