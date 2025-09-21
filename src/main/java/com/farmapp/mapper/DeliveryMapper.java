package com.farmapp.mapper;

import com.farmapp.dto.request.Delivery.DeliveryCreateRequestDTO;
import com.farmapp.dto.request.Delivery.DeliveryUpdateRequestDTO;
import com.farmapp.dto.response.Delivery.DeliveryDetailResponseDTO;
import com.farmapp.dto.response.Delivery.DeliverySummaryResponseDTO;
import com.farmapp.model.*;
import com.farmapp.enums.DeliveryStatus;

import java.time.format.DateTimeFormatter;

public class DeliveryMapper {



    /**
     * 1. Convert CreateRequestDTO → Entity
     */
    public static Delivery toEntity(DeliveryCreateRequestDTO dto, User farmer, Season season, Product product, Close close) {
        return Delivery.builder()
                .farmer(farmer)
                .season(season)
                .product(product)
                .close(close)
                .deliveredQuantity(dto.getDeliveredQuantity())
                .note(dto.getNote())
                .deliveredAt(dto.getDeliveredAt() != null ? dto.getDeliveredAt() : null)
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : null)
                .status(DeliveryStatus.PENDING) // mặc định là Pending, sẽ update lại nếu đủ số lượng
                .build();
    }

    /**
     * 2. Convert Entity → DetailResponseDTO (dùng để hiển thị từng lần giao)
     */
    public static DeliveryDetailResponseDTO toDetailResponse(Delivery delivery) {
        return DeliveryDetailResponseDTO.builder()
                .deliveryId(delivery.getId())
                .deliveredQuantity(delivery.getDeliveredQuantity())
                .note(delivery.getNote())
                .deliveredAt(delivery.getDeliveredAt()) // ✅ KHÔNG format nữa
                .build();
    }


    /**
     * 3. Convert Entity + tính toán → SummaryResponseDTO
     */
    public static DeliverySummaryResponseDTO toSummaryResponse(
            Delivery latestDelivery,
            Float totalRequired,
            Float totalDelivered,
            Float remainingQuantity
    ) {
        return DeliverySummaryResponseDTO.builder()
                .deliveryId(latestDelivery.getId())
                .productId(latestDelivery.getProduct().getId())
                .seasonId(latestDelivery.getSeason().getId())
                .farmerId(latestDelivery.getFarmer().getId())
                .closeId(latestDelivery.getClose().getId())
                .farmerName(latestDelivery.getFarmer().getName())
                .seasonName(latestDelivery.getSeason().getName())
                .productName(latestDelivery.getProduct().getName())
                .totalRequiredQuantity(totalRequired)
                .totalDeliveredQuantity(totalDelivered)
                .remainingQuantity(remainingQuantity)
                .status(latestDelivery.getStatus())
                .build();
    }

    /**
     * 4. Cập nhật Entity từ UpdateRequestDTO
     */
    public static void updateDeliveryFromDTO(Delivery delivery, DeliveryUpdateRequestDTO dto) {
        if (dto.getDeliveredQuantity() != null) {
            delivery.setDeliveredQuantity(dto.getDeliveredQuantity());
        }
        if (dto.getNote() != null) {
            delivery.setNote(dto.getNote());
        }
        if (dto.getDeliveredAt() != null) {
            delivery.setDeliveredAt(dto.getDeliveredAt());
        }
        // Status sẽ được xử lý lại trong Service sau khi tính tổng số lượng giao
    }
}
