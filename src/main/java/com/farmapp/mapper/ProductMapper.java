package com.farmapp.mapper;

import com.farmapp.dto.request.Product.ProductCreateRequestDTO;
import com.farmapp.dto.request.Product.ProductUpdateRequestDTO;
import com.farmapp.dto.response.Product.ProductResponseDTO;
import com.farmapp.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductCreateRequestDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .build();
    }
    public void updateEntity(Product product, ProductUpdateRequestDTO dto) {
        product.setName(dto.getName());
        product.setType(dto.getType());
        product.setDescription(dto.getDescription());
    }
    public ProductResponseDTO toResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .type(product.getType())
                .description(product.getDescription())
                .build();
    }
}
