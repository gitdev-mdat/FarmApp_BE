package com.farmapp.mapper;

import com.farmapp.dto.request.Close.CloseCreateRequestDTO;
import com.farmapp.dto.request.Close.CloseUpdateRequestDTO;
import com.farmapp.dto.response.Close.CloseResponseDTO;
import com.farmapp.model.*;

import org.springframework.stereotype.Component;

@Component
public class CloseMapper {

    public Close toEntity(CloseCreateRequestDTO dto, User farmer, Season season, Product product) {
        return Close.builder()
                .farmer(farmer)
                .season(season)
                .product(product)
                .totalQuantity(dto.getTotalQuantity())
                .price(dto.getPrice())
                .note(dto.getNote())
                .createdAt(dto.getCreatedAt())
                .isActive(true)
                .closeImageUrl(dto.getCloseImageUrl())
                .build();
    }

    public void updateEntity(Close close, CloseUpdateRequestDTO dto) {
        close.setTotalQuantity(dto.getTotalQuantity());
        close.setPrice(dto.getPrice());
        close.setNote(dto.getNote());
        close.setCreatedAt(dto.getCreatedAt());
    }

    public CloseResponseDTO toResponseDTO(Close close, String farmerName, String seasonName, String productName) {
        return CloseResponseDTO.builder()
                .id(close.getId())
                .farmerId(close.getFarmer().getId())
                .farmerName(farmerName)
                .seasonId(close.getSeason().getId())
                .seasonName(seasonName)
                .productId(close.getProduct().getId())
                .productName(productName)
                .totalQuantity(close.getTotalQuantity())
                .price(close.getPrice())
                .note(close.getNote())
                .createdAt(close.getCreatedAt())
                .closeImageUrl(close.getCloseImageUrl())
                .build();
    }
}
