package com.farmapp.dto.response.Payment;

import com.farmapp.enums.PaymentType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetailResponseDTO {
    private Integer paymentId;
    private LocalDate paymentDate;
    private Integer paymentAmount;
    private PaymentType paymentType;
    private String note;
    private String paymentImageUrl;
}
    