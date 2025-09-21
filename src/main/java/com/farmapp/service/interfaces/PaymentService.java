package com.farmapp.service.interfaces;

import com.farmapp.dto.request.Payment.PaymentCreateDetailRequestDTO;
import com.farmapp.dto.request.Payment.PaymentCreateSummaryRequestDTO;
import com.farmapp.dto.response.Payment.PaymentDetailResponseDTO;
import com.farmapp.dto.response.Payment.PaymentSummaryResponseDTO;
import com.farmapp.enums.TransactionType;

import java.util.List;

public interface PaymentService {

    // 1. Lấy danh sách Payment Summary theo filter (farmerId, seasonId, productId, transactionType)
    List<PaymentSummaryResponseDTO> getPaymentSummaryList(Integer farmerId, Integer productId, TransactionType transactionType);


    // 2. Lấy danh sách Payment Detail theo TransactionType + TransactionId
    List<PaymentDetailResponseDTO> getPaymentDetailList(TransactionType transactionType, String transactionId);

    // 3. Xoá mềm 1 payment (Detail) theo paymentId
    void deletePaymentDetail(Integer paymentId);

    // 4. Xoá cứng toàn bộ payment liên quan 1 transaction (Summary) theo transactionType + transactionId
    void deletePaymentSummary(String transactionType, String transactionId);

    //5. Tạo Payment Summary
    void createPaymentSummary(PaymentCreateSummaryRequestDTO dto);

    //6. Tạo Payment Detail
    void createPaymentDetail(PaymentCreateDetailRequestDTO dto);
}
