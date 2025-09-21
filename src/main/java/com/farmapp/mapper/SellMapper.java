package com.farmapp.mapper;

import com.farmapp.dto.request.Sell.SellCreateRequestDTO;
import com.farmapp.dto.request.Sell.SellUpdateRequestDTO;
import com.farmapp.dto.response.Sell.SellResponseDTO;
import com.farmapp.enums.SellType;
import com.farmapp.model.*;
import org.springframework.stereotype.Component;

@Component
public class SellMapper {

    public Sell toEntity(SellCreateRequestDTO dto,
                         User farmer,
                         Season season,
                         Product product,
                         Deposit deposit) {
        return Sell.builder()
                .price(dto.getPrice())
                .note(dto.getNote())
                .createdAt(dto.getCreatedAt())
                .farmer(farmer)
                .season(season)
                .product(product)
                .deposit(deposit)
                .totalQuantity(dto.getQuantityToSell())
                .sellType(deposit == null ? SellType.DIRECT : SellType.FROM_DEPOSIT)
                .sellImageUrl(dto.getSellImageUrl())
                .build();
    }

    public void updateEntity(Sell sell, SellUpdateRequestDTO dto) {
        sell.setPrice(dto.getPrice());
        sell.setNote(dto.getNote());
        sell.setCreatedAt(dto.getCreatedAt());
    }

    public SellResponseDTO toResponseDTO(
            Sell sell,
            String farmerName,
            String seasonName,
            String productName
    ) {
        return SellResponseDTO.builder()
                .id(sell.getId())
                .farmerId(sell.getFarmer().getId())
                .farmerName(farmerName)

                .seasonId(sell.getSeason().getId())
                .seasonName(seasonName)

                .productId(sell.getProduct().getId())
                .productName(productName)

                .price(sell.getPrice())
                .totalQuantity(sell.getTotalQuantity())
                .depositId(sell.getDeposit() != null ? sell.getDeposit().getId() : null)
                .createdAt(sell.getCreatedAt())
                .sellType(sell.getSellType())
                .sellImageUrl(sell.getSellImageUrl())
                .build();
    }
}

