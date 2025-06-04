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

import java.math.BigDecimal;
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

        System.out.println("Creating orders from cart for user: " + userId);
        System.out.println("Cart items count: " + cart.getItems().size());

        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User: " + user); // Log user

        // Group cart items by garden
        Map<Long, List<ShoppingCartItemDTO>> itemsByGarden = cart.getItems().stream()
                .collect(Collectors.groupingBy(item -> item.getGarden().getId()));

        List<OrderDTO> createdOrders = new ArrayList<>();

        // Create a separate order for each garden
        for (Map.Entry<Long, List<ShoppingCartItemDTO>> entry : itemsByGarden.entrySet()) {
            try {
                Long gardenId = entry.getKey();
                List<ShoppingCartItemDTO> gardenItems = entry.getValue();
                System.out.println("Processing garden " + gardenId + " with " + gardenItems.size() + " items");

                // Get garden
                Garden garden = gardenRepository.findById(gardenId)
                        .orElseThrow(() -> new RuntimeException("Garden not found: " + gardenId));
                System.out.println("Garden: " + garden); // Log garden

                // Create and save the order first
                final Order order = new Order();
                order.setUser(user);
                order.setGarden(garden);
                order.setDate(LocalDateTime.now());
                order.setStatus("PENDING");
                
                // Create order items for this garden's items
                List<OrderItem> orderItems = gardenItems.stream()
                        .map(cartItem -> {
                            try {
                                // Get the garden product
                                GardenProduct gardenProduct = gardenProductRepository.findById(cartItem.getGardenProductId())
                                        .orElseThrow(() -> new RuntimeException("Garden product not found: " + cartItem.getGardenProductId()));
                                System.out.println("Garden Product: " + gardenProduct);

                                // Verify the garden product belongs to the correct garden
                                if (!gardenProduct.getGarden().getId().equals(gardenId)) {
                                    throw new RuntimeException("Garden product does not belong to the correct garden");
                                }

                                OrderItem orderItem = new OrderItem();
                                orderItem.setOrder(order);
                                orderItem.setGardenProduct(gardenProduct);
                                orderItem.setQuantity(cartItem.getQuantity());
                                orderItem.setUnitPrice(gardenProduct.getUnitPrice());
                                orderItem.setTotalPrice(gardenProduct.getUnitPrice().multiply(cartItem.getQuantity()));
                                System.out.println("Order Item: " + orderItem);
                                return orderItemRepository.save(orderItem);
                            } catch (Exception e) {
                                throw new RuntimeException("Error creating order item: " + e.getMessage());
                            }
                        })
                        .toList();

                // Set items and calculate total price
                order.setItems(orderItems);
                BigDecimal totalPrice = orderItems.stream()
                        .map(OrderItem::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                order.setTotalPrice(totalPrice);
                System.out.println("Total Price: " + order.getTotalPrice());

                // Save the order with the total price
                Order savedOrder = orderRepository.save(order);
                createdOrders.add(orderMapper.toDTO(savedOrder));
                System.out.println("Order created successfully for garden " + gardenId);
            } catch (Exception e) {
                throw new RuntimeException("Error creating order for garden " + entry.getKey() + ": " + e.getMessage());
            }
        }

        // Clear the shopping cart after all orders are created successfully
        System.out.println("All orders created successfully. Clearing shopping cart for user: " + userId);
        try {
            shoppingCartService.clearCart(userId);
            System.out.println("Shopping cart cleared successfully");
        } catch (Exception e) {
            System.err.println("Error clearing shopping cart: " + e.getMessage());
            throw new RuntimeException("Error clearing shopping cart: " + e.getMessage());
        }

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
