package com.example.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private Long gardenId;
    private LocalDateTime date;
    private String status;
    private List<OrderItemDTO> items;
}