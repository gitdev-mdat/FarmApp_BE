package com.farmapp.dto.response.Delivery;

import com.farmapp.enums.DeliveryStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeliverySummaryResponseDTO {
    private Integer deliveryId;
    private String closeId;
    private String farmerName;
    private Integer farmerId;
    private String seasonName;
    private Integer seasonId;
    private String productName;
    private Integer productId;
    private Float totalRequiredQuantity;
    private Float totalDeliveredQuantity;
    private Float remainingQuantity;
    private DeliveryStatus status;
}

