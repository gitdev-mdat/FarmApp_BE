package com.farmapp.service.impl;

import com.farmapp.dto.request.Delivery.DeliveryCreateRequestDTO;
import com.farmapp.dto.request.Delivery.DeliveryUpdateRequestDTO;
import com.farmapp.dto.response.Delivery.DeliveryDetailResponseDTO;
import com.farmapp.dto.response.Delivery.DeliverySummaryResponseDTO;
import com.farmapp.enums.DeliveryStatus;
import com.farmapp.exception.FarmAppException;
import com.farmapp.mapper.DeliveryMapper;
import com.farmapp.model.*;
import com.farmapp.repository.*;
import com.farmapp.service.interfaces.DeliveryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final ProductRepository productRepository;
    private final CloseRepository closeRepository;

    @Override
    @Transactional
    public void createDelivery(DeliveryCreateRequestDTO dto) {
        Delivery delivery = mapAndValidateDelivery(dto);
        deliveryRepository.save(delivery);
        updateDeliveryStatus(delivery.getClose());
    }

    @Override
    @Transactional
    public void updateDelivery(Integer deliveryId, DeliveryUpdateRequestDTO dto) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy lần giao hàng", HttpStatus.NOT_FOUND));

        DeliveryMapper.updateDeliveryFromDTO(delivery, dto);
        deliveryRepository.save(delivery);
        updateDeliveryStatus(delivery.getClose());
    }

    @Override
    public List<DeliveryDetailResponseDTO> getDeliveryHistoryByCloseId(String closeId) {
        List<Delivery> deliveries = deliveryRepository.findAllByClose_Id(closeId);

        return deliveries.stream()
                .sorted(Comparator.comparing(
                        Delivery::getDeliveredAt,
                        Comparator.nullsLast(LocalDate::compareTo)
                ))
                .map(DeliveryMapper::toDetailResponse)
                .collect(Collectors.toList());
    }


    @Override
    public DeliverySummaryResponseDTO getDeliverySummaryByCloseId(String closeId) {
        List<Delivery> deliveries = deliveryRepository.findAllByClose_Id(closeId);
        if (deliveries.isEmpty()) {
            throw new FarmAppException("Không tìm thấy dữ liệu giao hàng", HttpStatus.NOT_FOUND);
        }

        Delivery latest = deliveries.stream()
                .max(Comparator.comparing(
                        Delivery::getDeliveredAt,
                        Comparator.nullsLast(LocalDate::compareTo)
                ))
                .orElse(deliveries.get(0));

        Float totalRequired = latest.getClose().getTotalQuantity();
        Float totalDelivered = deliveryRepository.sumDeliveredQuantityByCloseId(closeId);
        totalDelivered = totalDelivered != null ? totalDelivered : 0f;

        return DeliveryMapper.toSummaryResponse(
                latest, totalRequired, totalDelivered, totalRequired - totalDelivered
        );
    }

    @Override
    public List<DeliverySummaryResponseDTO> getAllDeliverySummary() {
        List<Delivery> deliveries = deliveryRepository.findAllByIsActiveTrue();

        return deliveries.stream()
                .collect(Collectors.groupingBy(delivery -> delivery.getClose().getId()))
                .values()
                .stream()
                .map(deliveryGroup -> {
                    // Fix: null-safe max() cho deliveredAt
                    Delivery latest = deliveryGroup.stream()
                            .max(Comparator.comparing(
                                    Delivery::getDeliveredAt,
                                    Comparator.nullsLast(LocalDate::compareTo)
                            ))
                            .orElse(deliveryGroup.get(0)); // fallback nếu tất cả đều null

                    Float totalRequired = latest.getClose().getTotalQuantity();
                    Float totalDelivered = deliveryRepository.sumDeliveredQuantityByCloseId(latest.getClose().getId());
                    totalDelivered = totalDelivered != null ? totalDelivered : 0f;
                    Float remaining = totalRequired - totalDelivered;

                    return DeliveryMapper.toSummaryResponse(latest, totalRequired, totalDelivered, remaining);
                })
                .collect(Collectors.toList());
    }



    @Override
    @Transactional
    public void deleteDelivery(Integer deliveryId) {
        Delivery delivery = deliveryRepository.findByIdAndIsActiveTrue(deliveryId)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy lần giao hàng", HttpStatus.NOT_FOUND));

        deliveryRepository.delete(delivery);
        updateDeliveryStatus(delivery.getClose());
    }

    // --------------------- PRIVATE METHODS ---------------------

    private Delivery mapAndValidateDelivery(DeliveryCreateRequestDTO dto) {
        User farmer = userRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new FarmAppException("Farmer không tồn tại", HttpStatus.NOT_FOUND));
        Season season = seasonRepository.findById(dto.getSeasonId())
                .orElseThrow(() -> new FarmAppException("Mùa vụ không tồn tại", HttpStatus.NOT_FOUND));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new FarmAppException("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND));
        Close close = closeRepository.findById(dto.getCloseId())
                .orElseThrow(() -> new FarmAppException("Đơn chốt không tồn tại", HttpStatus.NOT_FOUND));

        return DeliveryMapper.toEntity(dto, farmer, season, product, close);
    }

    private void updateDeliveryStatus(Close close) {
        Float totalDelivered = deliveryRepository.sumDeliveredQuantityByCloseId(close.getId());
        totalDelivered = totalDelivered != null ? totalDelivered : 0f;

        DeliveryStatus status = (Float.compare(totalDelivered, close.getTotalQuantity()) >= 0)
                ? DeliveryStatus.COMPLETED
                : DeliveryStatus.PENDING;

        List<Delivery> deliveries = deliveryRepository.findAllByClose_Id(close.getId());
        deliveries.forEach(delivery -> delivery.setStatus(status));
        deliveryRepository.saveAll(deliveries);
    }
}
