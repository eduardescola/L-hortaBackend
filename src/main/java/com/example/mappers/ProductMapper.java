package com.example.mappers;

import com.example.dto.ProductDTO;
import com.example.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
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

    public List<ProductDTO> toDTOList(List<Product> products) {
        if (products == null) {
            return null;
        }

        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}