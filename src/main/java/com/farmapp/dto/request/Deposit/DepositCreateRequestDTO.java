package com.farmapp.dto.request.Deposit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositCreateRequestDTO {
    private Integer farmerId;
    private Integer seasonId;
    private Integer productId;
    private Float totalQuantity;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;
    private String note;
    private String depositImageUrl;
}
