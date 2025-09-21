package com.farmapp.mapper;

import com.farmapp.dto.request.Deposit.DepositCreateRequestDTO;
import com.farmapp.dto.request.Deposit.DepositUpdateRequestDTO;
import com.farmapp.dto.response.Deposit.DepositResponseDTO;
import com.farmapp.model.Deposit;
import org.springframework.stereotype.Component;

@Component
public class DepositMapper {

    public Deposit toEntity(DepositCreateRequestDTO dto) {
        return Deposit.builder()
                .totalQuantity(dto.getTotalQuantity())
                .remainQuantity(dto.getTotalQuantity()) // mặc định remain = total
                .note(dto.getNote())
                .createdAt(dto.getCreatedAt())
                .depositImageUrl(dto.getDepositImageUrl())
                .build();
    }

    public void updateEntity(Deposit deposit, DepositUpdateRequestDTO dto) {
        deposit.setNote(dto.getNote());
        deposit.setCreatedAt(dto.getCreatedAt());
        // remainQuantity sẽ tính lại bên service khi có phát sinh giao dịch
    }

    public DepositResponseDTO toResponseDTO(
            Deposit deposit,
            String farmerName,
            String seasonName,
            String productName
    ) {
        float soldQuantity = deposit.getTotalQuantity() - deposit.getRemainQuantity();
        float soldProgress = deposit.getTotalQuantity() == 0 ? 0f :
                (soldQuantity / deposit.getTotalQuantity()) * 100f;

        return DepositResponseDTO.builder()
                .id(deposit.getId())
                .farmerId(deposit.getFarmer().getId())
                .farmerName(farmerName)

                .seasonId(deposit.getSeason().getId())
                .seasonName(seasonName)

                .productId(deposit.getProduct().getId())
                .productName(productName)

                .totalQuantity(deposit.getTotalQuantity())
                .soldQuantity(soldQuantity)
                .soldProgress(soldProgress)

                .note(deposit.getNote())
                .createdAt(deposit.getCreatedAt())
                .depositImageUrl(deposit.getDepositImageUrl())
                .build();
    }
}
