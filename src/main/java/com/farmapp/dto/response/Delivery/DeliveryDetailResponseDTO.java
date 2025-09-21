package com.farmapp.dto.response.Delivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryDetailResponseDTO {
    private Integer deliveryId;
    private Float deliveredQuantity;
    private String note;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deliveredAt;

}
