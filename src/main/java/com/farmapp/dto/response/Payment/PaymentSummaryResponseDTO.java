package com.farmapp.dto.response.Payment;

import com.farmapp.enums.PaymentStatus;
import com.farmapp.enums.TransactionType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSummaryResponseDTO {
    private Integer paymentId;
    private Integer farmerId;
    private String farmerName;
    private Integer seasonId;
    private String seasonName;
    private Integer productId;
    private String productName;
    private TransactionType transactionType;
    private String transactionId;
    private Integer amountDue; // tong so tien phai tra, o close va sell da co san
    private Integer amountPaid; //  tổng so tien da tra, xu ly o service
    private PaymentStatus paymentStatus;
}
