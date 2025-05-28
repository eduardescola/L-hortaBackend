package com.example.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GardenProductDTO {
    private Long id;
    private String caName;
    private String esName;
    private String enName;
    private String frName;
    private BigDecimal stock;
    private BigDecimal unitPrice;
    private String units;
}