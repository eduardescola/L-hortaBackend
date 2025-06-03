package com.example.mappers;

import com.example.dto.OrderDTO;
import com.example.dto.OrderItemDTO;
import com.example.dto.ProductDTO;
import com.example.entities.Order;
import com.example.entities.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private ProductMapper productMapper;


    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setGardenId(order.getGarden().getId());
        dto.setDate(order.getDate());
        dto.setStatus(order.getStatus());
        dto.setItems(toItemDTOList(order.getItems()));

        return dto;
    }

    public List<OrderDTO> toDTOList(List<Order> orders) {
        if (orders == null) {
            return null;
        }

        return orders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public OrderItemDTO toItemDTO(OrderItem item) {
        if (item == null) {
            return null;
        }

        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());

        productMapper.toDTO(item.getGardenProduct().getProduct());

        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setUnits(item.getGardenProduct().getUnits());

        return dto;
    }

    public List<OrderItemDTO> toItemDTOList(List<OrderItem> items) {
        if (items == null) {
            return null;
        }

        return items.stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
    }
}