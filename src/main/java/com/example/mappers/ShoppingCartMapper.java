package com.example.mappers;

import com.example.dto.ShoppingCartDTO;
import com.example.dto.ShoppingCartItemDTO;
import com.example.dto.ProductDTO;
import com.example.entities.ShoppingCart;
import com.example.entities.ShoppingCartItem;
import com.example.entities.GardenProduct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.dto.GardenDTO;

import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper {

    @Autowired
    private ProductMapper productMapper;

    public ShoppingCartDTO toDTO(ShoppingCart cart) {
        if (cart == null) {
            return null;
        }

        ShoppingCartDTO dto = new ShoppingCartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setCreatedAt(cart.getCreatedAt());

        if (cart.getItems() != null) {
            dto.setItems(cart.getItems().stream()
                    .map(this::toItemDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public ShoppingCartItemDTO toItemDTO(ShoppingCartItem item) {
        if (item == null) {
            return null;
        }

        ShoppingCartItemDTO dto = new ShoppingCartItemDTO();
        dto.setId(item.getId());
        dto.setGardenProductId(item.getGardenProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setAddedAt(item.getAddedAt());

        GardenProduct gardenProduct = item.getGardenProduct();
        dto.setUnitPrice(gardenProduct.getUnitPrice());
        dto.setUnits(gardenProduct.getUnits());

        dto.setProduct(productMapper.toDTO(gardenProduct.getProduct()));
        dto.setGarden(toGardenDTO(gardenProduct.getGarden()));

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

    private GardenDTO toGardenDTO(com.example.entities.Garden garden) {
        if (garden == null) {
            return null;
        }

        GardenDTO dto = new GardenDTO();
        dto.setId(garden.getId());
        dto.setName(garden.getName());

        return dto;
    }
}