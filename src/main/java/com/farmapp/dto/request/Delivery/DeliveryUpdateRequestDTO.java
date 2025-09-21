package com.farmapp.dto.request.Delivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryUpdateRequestDTO {
    // closeId lay ngoai service
    private Float deliveredQuantity;
    private String note;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deliveredAt;
    }
