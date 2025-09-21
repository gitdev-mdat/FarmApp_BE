package com.farmapp.mapper;

import com.farmapp.dto.request.Payment.PaymentCreateDetailRequestDTO;
import com.farmapp.dto.request.Payment.PaymentCreateSummaryRequestDTO;
import com.farmapp.dto.response.Payment.PaymentDetailResponseDTO;
import com.farmapp.dto.response.Payment.PaymentSummaryResponseDTO;
import com.farmapp.enums.PaymentStatus;
import com.farmapp.enums.TransactionType;
import com.farmapp.model.Payment;
import com.farmapp.model.Product;
import com.farmapp.model.Season;
import com.farmapp.model.User;

public class PaymentMapper {

    // 1. Tạo Payment từ DetailRequest (Form create)
    public static Payment toEntity(PaymentCreateDetailRequestDTO dto, User farmer, Season season, Product product) {
        return Payment.builder()
                .farmer(farmer)
                .season(season)
                .product(product)
                .transactionType(dto.getTransactionType())
                .transactionId(dto.getTransactionId())
                .paymentDate(dto.getPaymentDate())
                .paymentAmount(dto.getPaymentAmount())
                .type(dto.getType())
                .paymentImageUrl(dto.getPaymentImageUrl())
                .note(dto.getNote())
                .build();
    }

    // 2. Convert Entity → DetailResponse
    public static PaymentDetailResponseDTO toDetailResponse(Payment payment) {
        return PaymentDetailResponseDTO.builder()
                .paymentId(payment.getId())
                .paymentDate(payment.getPaymentDate())
                .paymentAmount(payment.getPaymentAmount())
                .paymentType(payment.getType())
                .note(payment.getNote())
                .paymentImageUrl(payment.getPaymentImageUrl())
                .build();
    }

    // 3. Convert dữ liệu tổng hợp → SummaryResponse
    public static PaymentSummaryResponseDTO toSummaryResponse(
            Integer paymentId,
            User farmer,
            Season season,
            Product product,
            String transactionId,
            TransactionType transactionType,
            Integer amountDue,
            Integer amountPaid,
            PaymentStatus paymentStatus
    ) {
        return PaymentSummaryResponseDTO.builder()
                .paymentId(paymentId)
                .farmerId(farmer.getId())
                .farmerName(farmer.getName())
                .seasonId(season.getId())
                .seasonName(season.getName())
                .productId(product.getId())
                .productName(product.getName())
                .transactionId(transactionId)
                .transactionType(transactionType)
                .amountDue(amountDue)
                .amountPaid(amountPaid)
                .paymentStatus(paymentStatus)
                .build();
    }

    // (Optional) 4. Convert từ SummaryRequest → Payment Entity (nếu cần query cứng)
    public static Payment toEntityFromSummaryRequest(PaymentCreateSummaryRequestDTO dto, User farmer, Season season, Product product) {
        return Payment.builder()
                .farmer(farmer)
                .season(season)
                .product(product)
                .transactionType(dto.getTransactionType())
                .transactionId(dto.getTransactionId())
                .build();
    }
}

