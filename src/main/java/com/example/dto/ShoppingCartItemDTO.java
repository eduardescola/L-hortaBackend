package com.example.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShoppingCartItemDTO {
    private Long id;
    private Long gardenProductId;
    private ProductDTO product;
    private GardenDTO garden;
    private BigDecimal unitPrice;
    private String units;
    private BigDecimal quantity;
    private LocalDateTime addedAt;
}