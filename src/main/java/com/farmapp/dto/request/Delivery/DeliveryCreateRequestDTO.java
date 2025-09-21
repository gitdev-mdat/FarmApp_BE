package com.farmapp.dto.request.Delivery;

import com.farmapp.enums.DeliveryStatus;
import com.farmapp.model.Delivery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryCreateRequestDTO {
    private Integer farmerId;
    private Integer seasonId;
    private Integer productId;
    private String closeId;
    private Float deliveredQuantity; // Danh cho cap nhap lan giao moi cua don CloseId nay
    private String note;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deliveredAt;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;
}
