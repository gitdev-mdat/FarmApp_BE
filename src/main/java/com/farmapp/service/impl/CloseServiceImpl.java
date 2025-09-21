package com.farmapp.service.impl;

import com.farmapp.dto.request.Close.CloseCreateRequestDTO;
import com.farmapp.dto.request.Close.CloseUpdateRequestDTO;
import com.farmapp.dto.request.Delivery.DeliveryCreateRequestDTO;
import com.farmapp.dto.request.Payment.PaymentCreateSummaryRequestDTO;
import com.farmapp.dto.response.Close.CloseResponseDTO;
import com.farmapp.enums.PaymentStatus;
import com.farmapp.enums.SeasonStatus;
import com.farmapp.enums.TransactionType;
import com.farmapp.exception.FarmAppException;
import com.farmapp.helper.IdGenerator;
import com.farmapp.mapper.CloseMapper;
import com.farmapp.model.*;
import com.farmapp.repository.*;
import com.farmapp.service.interfaces.CloseService;
import com.farmapp.service.interfaces.DeliveryService;
import com.farmapp.service.interfaces.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CloseServiceImpl implements CloseService {

    private final CloseRepository closeRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final IdGenerator idGenerator;
    private final CloseMapper closeMapper;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    // Tạo Close mới và tạo thêm
    @Override
    @Transactional
    public CloseResponseDTO createClose(CloseCreateRequestDTO dto) {
        User farmer = userRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new FarmAppException("Farmer không tồn tại", HttpStatus.NOT_FOUND));
        Season season = seasonRepository.findById(dto.getSeasonId())
                .orElseThrow(() -> new FarmAppException("Mùa vụ không tồn tại", HttpStatus.NOT_FOUND));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new FarmAppException("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND));

        Close close = closeMapper.toEntity(dto, farmer, season, product);
        close.setId(idGenerator.generateCloseId());
        close.setAmountDue((int) Math.round(close.getTotalQuantity() * close.getPrice()));
        close.setAmountPaid(0);
        close.setPaymentStatus(PaymentStatus.UNPAID);
        close = closeRepository.save(close);
        DeliveryCreateRequestDTO deliveryDTO = buildInitialDeliveryDTO(dto,close.getId());
        deliveryService.createDelivery(deliveryDTO);
        // 3. Tạo Payment ban đầu
        paymentService.createPaymentSummary(buildInitialPaymentSummary(dto, close.getId()));
        return closeMapper.toResponseDTO(close, farmer.getName(), season.getName(), product.getName());
    }

    // Cập nhật Close
    @Override
    @Transactional
    public CloseResponseDTO updateClose(String id, CloseUpdateRequestDTO dto) {
        Close close = closeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy Close", HttpStatus.NOT_FOUND));

        closeMapper.updateEntity(close, dto);

        close = closeRepository.save(close);

        return closeMapper.toResponseDTO(close,
                close.getFarmer().getName(),
                close.getSeason().getName(),
                close.getProduct().getName());
    }

    @Override
    @Transactional
    public void deleteClose(String id) {
        Close close = closeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy Close", HttpStatus.NOT_FOUND));

        // Check đã phát sinh giao hàng chưa
        if (closeRepository.existsAnyDeliveryWithQuantity(close.getId())) {
            throw new FarmAppException("Không thể xoá vì đã phát sinh giao hàng", HttpStatus.BAD_REQUEST);
        }

        // Check đã phát sinh thanh toán chưa
        List<Payment> payments = paymentRepository.findAllByTransactionTypeAndTransactionIdAndIsActiveTrue(TransactionType.CLOSE, close.getId());
        boolean hasPaymentMade = payments.stream().anyMatch(p -> p.getPaymentAmount() != null && p.getPaymentAmount() > 0);

        if (hasPaymentMade) {
            throw new FarmAppException("Không thể xoá vì đã có thanh toán", HttpStatus.BAD_REQUEST);
        }

        // Soft delete Close
        close.setIsActive(false);
        closeRepository.save(close);

        // Soft delete Delivery liên quan
        List<Delivery> relatedDeliveries = close.getDeliveries();
        if (relatedDeliveries != null) {
            relatedDeliveries.forEach(delivery -> delivery.setIsActive(false));
            deliveryRepository.saveAll(relatedDeliveries);
        }

        // Soft delete Payment liên quan
        payments.forEach(p -> p.setIsActive(false));
        paymentRepository.saveAll(payments);
    }




    // Lấy danh sách Close thuộc mùa vụ đang diễn ra
    @Override
    public List<CloseResponseDTO> getAllCloses() {
        List<Season> onGoingSeasons = seasonRepository.findByStatus(SeasonStatus.ONGOING);
        if (onGoingSeasons.isEmpty()) {
            throw new FarmAppException("Không có mùa vụ đang diễn ra", HttpStatus.NOT_FOUND);
        }
        List<Integer> seasonIds = onGoingSeasons.stream().map(Season::getId).toList();

        return closeRepository.findAllInOnGoingSeasons().stream()
                .filter(c -> seasonIds.contains(c.getSeason().getId()))
                .map(this::mapWithNames)
                .collect(Collectors.toList());
    }

    // Lọc Close theo farmer, season, product
    @Override
    public List<CloseResponseDTO> filterCloses(Integer farmerId, Integer seasonId, Integer productId) {
        return closeRepository.filterCloses(farmerId, seasonId, productId).stream()
                .map(this::mapWithNames)
                .collect(Collectors.toList());
    }

    // Lấy chi tiết Close theo ID
    @Override
    public CloseResponseDTO getById(String id) {
        Close close = closeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy Close", HttpStatus.NOT_FOUND));
        return mapWithNames(close);
    }

    // Map Close sang DTO với tên thực thể
    private CloseResponseDTO mapWithNames(Close close) {
        String farmerName = close.getFarmer().getName();
        String seasonName = close.getSeason().getName();
        String productName = close.getProduct().getName();

        return closeMapper.toResponseDTO(close, farmerName, seasonName, productName);
    }
    // Tạo 1 đơn Giao hàng ngay sau khi Chốt
    private DeliveryCreateRequestDTO buildInitialDeliveryDTO(CloseCreateRequestDTO dto, String closeId) {
        return DeliveryCreateRequestDTO.builder()
                .farmerId(dto.getFarmerId())
                .seasonId(dto.getSeasonId())
                .productId(dto.getProductId())
                .closeId(closeId)
                .createdAt(LocalDate.now())
                .build();
    }
    // Tạo 1 payment Summary
    private PaymentCreateSummaryRequestDTO buildInitialPaymentSummary(CloseCreateRequestDTO dto, String closeId) {
        return PaymentCreateSummaryRequestDTO.builder()
                .farmerId(dto.getFarmerId())
                .seasonId(dto.getSeasonId())
                .productId(dto.getProductId())
                .transactionId(closeId)
                .transactionType(TransactionType.CLOSE)
                .build();
    }

}
