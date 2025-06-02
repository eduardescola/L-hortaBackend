package com.example.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private Long gardenProductId;
    private ProductDTO product;
    private GardenDTO garden;
    private String productName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String units;
}