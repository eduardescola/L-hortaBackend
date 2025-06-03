package com.example.services;

import com.example.dto.OrderDTO;
import com.example.dto.ShoppingCartDTO;
import com.example.dto.ShoppingCartItemDTO;
import com.example.entities.*;
import com.example.mappers.OrderMapper;
import com.example.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private GardenProductRepository gardenProductRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public List<OrderDTO> findByUserId(Long userId) {
        return orderMapper.toDTOList(orderRepository.findByUserIdOrderByDateDesc(userId));
    }

    @Transactional
    public List<OrderDTO> createOrdersFromCart(Long userId) {
        // Get the shopping cart
        ShoppingCartDTO cart = shoppingCartService.getCart(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }

        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Group cart items by garden
        Map<Long, List<ShoppingCartItemDTO>> itemsByGarden = cart.getItems().stream()
                .collect(Collectors.groupingBy(item -> item.getGarden().getId()));

        List<OrderDTO> createdOrders = new ArrayList<>();

        // Create a separate order for each garden
        for (Map.Entry<Long, List<ShoppingCartItemDTO>> entry : itemsByGarden.entrySet()) {
            Long gardenId = entry.getKey();
            List<ShoppingCartItemDTO> gardenItems = entry.getValue();

            // Get garden
            Garden garden = gardenRepository.findById(gardenId)
                    .orElseThrow(() -> new RuntimeException("Garden not found: " + gardenId));

            // Create and save the order first
            final Order order = orderRepository.save(new Order() {{
                setUser(user);
                setGarden(garden);
                setDate(LocalDateTime.now());
                setStatus("PENDING");
            }});

            // Create order items for this garden's items
            List<OrderItem> orderItems = gardenItems.stream()
                    .map(cartItem -> {
                        // Get the garden product
                        GardenProduct gardenProduct = gardenProductRepository.findById(cartItem.getGardenProductId())
                                .orElseThrow(() -> new RuntimeException("Garden product not found: " + cartItem.getGardenProductId()));

                        // Verify the garden product belongs to the correct garden
                        if (!gardenProduct.getGarden().getId().equals(gardenId)) {
                            throw new RuntimeException("Garden product does not belong to the correct garden");
                        }

                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrder(order);
                        //orderItem.setProduct(gardenProduct.getProduct());
                        orderItem.setGardenProduct(gardenProduct);
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setUnitPrice(gardenProduct.getUnitPrice());
                        return orderItemRepository.save(orderItem);
                    })
                    .toList();

            order.setItems(orderItems);
            Order savedOrder = orderRepository.save(order);
            createdOrders.add(orderMapper.toDTO(savedOrder));
        }

        // Clear the shopping cart after all orders are created
        shoppingCartService.clearCart(userId);

        return createdOrders;
    }

    public List<OrderDTO> findAll() {
        return orderMapper.toDTOList(orderRepository.findAll());
    }

    public Optional<OrderDTO> findById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDTO);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
