package com.example.services;

import com.example.dto.OrderDTO;
import com.example.dto.OrderItemDTO;
import com.example.dto.ProductDTO;
import com.example.dto.ShoppingCartDTO;
import com.example.entities.*;
import com.example.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.entities.GardenProduct;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private ProductRepository productRepository;
    @Autowired
    private GardenProductRepository gardenProductRepository;

    @Transactional
    public OrderDTO createOrderFromCart(Long userId) {
        // Get the shopping cart
        ShoppingCartDTO cart = shoppingCartService.getCart(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }

        // Get user and garden
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the garden from the first item (assuming all items are from the same garden)
        Long gardenId = cart.getItems().get(0).getGarden().getId();
        Garden garden = gardenRepository.findById(gardenId)
                .orElseThrow(() -> new RuntimeException("Garden not found"));

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setGarden(garden);
        order.setDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order = orderRepository.save(order);

        // Create order items from cart items
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    //orderItem.setOrder(order);
                    orderItem.setGardenProduct(gardenProductRepository.findById(cartItem.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException("Product not found")));
                    //orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setUnitPrice(cartItem.getUnitPrice());
                    return orderItemRepository.save(orderItem);
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);
        order = orderRepository.save(order);

        // Clear the shopping cart
        shoppingCartService.clearCart(userId);

        return convertToDTO(order);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setGardenId(order.getGarden().getId());
        dto.setDate(order.getDate());
        dto.setStatus(order.getStatus());

        dto.setItems(order.getItems().stream()
                .map(this::convertToItemDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private ProductDTO toProductDTO(com.example.entities.Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setCaName(product.getCaName());
        dto.setEsName(product.getEsName());
        dto.setEnName(product.getEnName());
        dto.setFrName(product.getFrName());
        dto.setImage(product.getImage());

        return dto;
    }

    private OrderItemDTO convertToItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());

        GardenProduct gardenProduct = item.getGardenProduct();
        dto.setProduct(toProductDTO(gardenProduct.getProduct()));
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }

    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> findById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
