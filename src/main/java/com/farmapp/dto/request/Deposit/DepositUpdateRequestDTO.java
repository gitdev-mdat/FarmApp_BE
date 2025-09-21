package com.farmapp.dto.request.Deposit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositUpdateRequestDTO {
    private Integer farmerId;
    private Integer seasonId;
    private Integer productId;
    private String note;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;
}

