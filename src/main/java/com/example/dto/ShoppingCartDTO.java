package com.example.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShoppingCartDTO {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private List<ShoppingCartItemDTO> items;
}