package com.farmapp.service.impl;

import com.farmapp.dto.request.Payment.PaymentCreateDetailRequestDTO;
import com.farmapp.dto.request.Payment.PaymentCreateSummaryRequestDTO;
import com.farmapp.dto.response.Payment.PaymentDetailResponseDTO;
import com.farmapp.dto.response.Payment.PaymentSummaryResponseDTO;
import com.farmapp.enums.PaymentStatus;
import com.farmapp.enums.TransactionType;
import com.farmapp.exception.FarmAppException;
import com.farmapp.mapper.PaymentMapper;
import com.farmapp.model.*;
import com.farmapp.repository.*;
import com.farmapp.service.interfaces.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CloseRepository closeRepository;
    private final SellRepository sellRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    @Override
    public List<PaymentSummaryResponseDTO> getPaymentSummaryList(Integer farmerId, Integer productId, TransactionType transactionType) {
        List<Payment> payments = paymentRepository.findAllInOnGoingSeasons(farmerId, productId);

        // Group theo transactionId
        Map<String, List<Payment>> groupedPayments = payments.stream()
                .filter(p -> transactionType == null || p.getTransactionType() == transactionType) // Optional filter
                .collect(Collectors.groupingBy(Payment::getTransactionId));

        List<PaymentSummaryResponseDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<Payment>> entry : groupedPayments.entrySet()) {
            String transactionId = entry.getKey();
            List<Payment> paymentList = entry.getValue();

            Payment firstPayment = paymentList.get(0);
            Integer amountPaid = paymentList.stream().mapToInt(p -> p.getPaymentAmount() != null ? p.getPaymentAmount() : 0).sum();
            Integer amountDue = getAmountDueFromTransaction(firstPayment.getTransactionType(), transactionId);

            PaymentSummaryResponseDTO dto = PaymentMapper.toSummaryResponse(
                    firstPayment.getId(),
                    firstPayment.getFarmer(),
                    firstPayment.getSeason(),
                    firstPayment.getProduct(),
                    transactionId,
                    firstPayment.getTransactionType(),
                    amountDue,
                    amountPaid,
                    calculatePaymentStatus(amountPaid, amountDue)
            );
            result.add(dto);
        }
        return result;
    }
    private Integer getAmountDueFromTransaction(TransactionType type, String transactionId) {
        if (type == TransactionType.CLOSE) {
            return closeRepository.findById(transactionId)
                    .map(c -> {
                        Float totalQuantity = c.getTotalQuantity() != null ? c.getTotalQuantity() : 0f;
                        Integer price = c.getPrice() != null ? c.getPrice() : 0;
                        return Math.round(totalQuantity * price);
                    })
                    .orElse(0);
        } else if (type == TransactionType.SELL_DIRECT || type == TransactionType.SELL_FROM_DEPOSIT) {
            return sellRepository.findById(transactionId)
                    .map(s -> {
                        Float totalQuantity = s.getTotalQuantity() != null ? s.getTotalQuantity() : 0f;
                        Integer price = s.getPrice() != null ? s.getPrice() : 0;
                        return Math.round(totalQuantity * price);
                    })
                    .orElse(0);
        }
        return 0;
    }


    private PaymentStatus calculatePaymentStatus(Integer paid, Integer due) {
        if (paid == null) paid = 0;
        if (due == null || due == 0) return PaymentStatus.UNPAID;

        if (paid == 0) return PaymentStatus.UNPAID;
        if (paid < due) return PaymentStatus.PARTIALLY_PAID;
        return PaymentStatus.PAID;
    }



    @Override
    public List<PaymentDetailResponseDTO> getPaymentDetailList(TransactionType transactionType, String transactionId) {

        List<Payment> payments = paymentRepository.findAllByTransactionTypeAndTransactionIdAndIsActiveTrueAndPaymentDateNotNull(transactionType, transactionId);

        return payments.stream()
                .map(PaymentMapper::toDetailResponse)
                .collect(Collectors.toList());
    }


    @Override
    public void deletePaymentDetail(Integer paymentId) {
        Payment payment = paymentRepository.findByIdAndIsActiveTrue(paymentId)
                .orElseThrow(() -> new FarmAppException("Payment không tồn tại", HttpStatus.NOT_FOUND));
        payment.setIsActive(false);
        paymentRepository.save(payment);
    }


    @Override
    public void deletePaymentSummary(String transactionTypeStr, String transactionId) {
        TransactionType transactionType = TransactionType.valueOf(transactionTypeStr.toUpperCase());
        List<Payment> payments = paymentRepository.findAllByTransactionTypeAndTransactionIdAndIsActiveTrue(transactionType, transactionId);

        if (payments.isEmpty()) {
            throw new FarmAppException("Không tìm thấy dữ liệu thanh toán để xoá", HttpStatus.NOT_FOUND);
        }
        paymentRepository.deleteAll(payments);

    }


    @Override
    public void createPaymentSummary(PaymentCreateSummaryRequestDTO dto) {
        User farmer = userRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new FarmAppException("Farmer không tồn tại", HttpStatus.NOT_FOUND));

        Season season = seasonRepository.findById(dto.getSeasonId())
                .orElseThrow(() -> new FarmAppException("Mùa vụ không tồn tại", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new FarmAppException("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND));

        Payment payment = PaymentMapper.toEntityFromSummaryRequest(dto, farmer, season, product);
        payment.setIsActive(true);
        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void createPaymentDetail(PaymentCreateDetailRequestDTO dto) {
        User farmer = userRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new FarmAppException("Farmer không tồn tại", HttpStatus.NOT_FOUND));

        Season season = seasonRepository.findById(dto.getSeasonId())
                .orElseThrow(() -> new FarmAppException("Mùa vụ không tồn tại", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new FarmAppException("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND));

        if (dto.getTransactionId() == null) {
            throw new FarmAppException("Thiếu transactionId", HttpStatus.BAD_REQUEST);
        }

        Payment payment = PaymentMapper.toEntity(dto, farmer, season, product);
        payment.setIsActive(true);

        if (dto.getTransactionType() == TransactionType.SELL_DIRECT || dto.getTransactionType() == TransactionType.SELL_FROM_DEPOSIT) {
            Sell sell = sellRepository.findById(dto.getTransactionId())
                    .orElseThrow(() -> new FarmAppException("Sell không tồn tại", HttpStatus.NOT_FOUND));

            Integer updatedAmount = sell.getAmountPaid() + dto.getPaymentAmount();
            sell.setAmountPaid(updatedAmount);

            if (updatedAmount < sell.getAmountDue() && updatedAmount != 0) {
                sell.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
            } else if (updatedAmount == sell.getAmountDue()) {
                sell.setPaymentStatus(PaymentStatus.PAID);
            }

            sellRepository.save(sell);
        }

        paymentRepository.save(payment);
    }

}
