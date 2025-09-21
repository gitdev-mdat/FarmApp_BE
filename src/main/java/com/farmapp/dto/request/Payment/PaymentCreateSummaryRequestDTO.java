package com.farmapp.dto.request.Payment;

import com.farmapp.enums.TransactionType;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCreateSummaryRequestDTO {
    private Integer farmerId;
    private Integer seasonId;
    private Integer productId;
    private TransactionType transactionType;
    private String transactionId;
}
