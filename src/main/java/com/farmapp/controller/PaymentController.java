package com.farmapp.controller;

import com.farmapp.dto.request.Payment.PaymentCreateDetailRequestDTO;
import com.farmapp.dto.request.Payment.PaymentCreateSummaryRequestDTO;
import com.farmapp.dto.response.Payment.PaymentDetailResponseDTO;
import com.farmapp.dto.response.Payment.PaymentSummaryResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.enums.TransactionType;
import com.farmapp.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 1. Lấy danh sách Payment Summary theo filter (farmerId, productId, transactionType)
     */
    @GetMapping("/summary")
    public ResponseEntity<GlobalResponse<List<PaymentSummaryResponseDTO>>> getPaymentSummaryList(
            @RequestParam(required = false) Integer farmerId,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) TransactionType transactionType
    ) {
        List<PaymentSummaryResponseDTO> result = paymentService.getPaymentSummaryList(farmerId, productId, transactionType);
        return ResponseEntity.ok(GlobalResponse.success(result));
    }

    /**
     * 2. Lấy danh sách Payment Detail theo TransactionType + TransactionId
     */
    @GetMapping("/detail")
    public ResponseEntity<GlobalResponse<List<PaymentDetailResponseDTO>>> getPaymentDetailList(
            @RequestParam TransactionType transactionType,
            @RequestParam String transactionId
    ) {
        List<PaymentDetailResponseDTO> result = paymentService.getPaymentDetailList(transactionType, transactionId);
        return ResponseEntity.ok(GlobalResponse.success(result));
    }

    /**
     * 3. Xoá mềm một Payment Detail theo paymentId
     */
    @DeleteMapping("/detail/{paymentId}")
    public ResponseEntity<GlobalResponse<Void>> deletePaymentDetail(@PathVariable Integer paymentId) {
        paymentService.deletePaymentDetail(paymentId);
        return ResponseEntity.ok(GlobalResponse.success(null));
    }

    /**
     * 4. Tạo mới Payment Detail
     */
    @PostMapping("/detail")
    public ResponseEntity<GlobalResponse<Void>> createPaymentDetail(
            @RequestBody PaymentCreateDetailRequestDTO dto
    ) {
        paymentService.createPaymentDetail(dto);
        return ResponseEntity.ok(GlobalResponse.success(null));
    }
}
