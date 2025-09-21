package com.farmapp.dto.request.Payment;

import com.farmapp.enums.PaymentType;
import com.farmapp.enums.TransactionType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCreateDetailRequestDTO {
    private Integer farmerId;
    private Integer seasonId;
    private Integer productId;
    private TransactionType transactionType;
    private String transactionId;
    private LocalDate paymentDate;
    private Integer paymentAmount;
    private PaymentType type;
    private String paymentImageUrl;
    private String note;
}
