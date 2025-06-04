package com.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private Long gardenId;
    private String gardenName;
    private LocalDateTime date;
    private String status;
    private BigDecimal totalPrice;
    private List<OrderItemDTO> items;
}