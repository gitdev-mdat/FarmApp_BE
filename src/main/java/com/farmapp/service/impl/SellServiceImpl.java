package com.farmapp.service.impl;

import com.farmapp.dto.request.Payment.PaymentCreateSummaryRequestDTO;
import com.farmapp.dto.request.Sell.SellCreateRequestDTO;
import com.farmapp.dto.request.Sell.SellUpdateRequestDTO;
import com.farmapp.dto.response.Sell.SellResponseDTO;
import com.farmapp.enums.PaymentStatus;
import com.farmapp.enums.SellType;
import com.farmapp.enums.TransactionType;
import com.farmapp.exception.FarmAppException;
import com.farmapp.helper.IdGenerator;
import com.farmapp.mapper.SellMapper;
import com.farmapp.model.*;
import com.farmapp.repository.*;
import com.farmapp.service.interfaces.PaymentService;
import com.farmapp.service.interfaces.SellService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellServiceImpl implements SellService {

    private final SellRepository sellRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final ProductRepository productRepository;
    private final DepositRepository depositRepository;

    private final PaymentService paymentService;
    private final SellMapper sellMapper;
    private final IdGenerator idGenerator;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public SellResponseDTO createSellFromDeposit(SellCreateRequestDTO dto) {
        if (dto.getDepositId() == null)
            throw new FarmAppException("DepositId là bắt buộc cho giao dịch FROM_DEPOSIT", HttpStatus.BAD_REQUEST);

        // Fetch entities
        User farmer = userRepository.findById(dto.getFarmerId()).orElseThrow();
        Season season = seasonRepository.findById(dto.getSeasonId()).orElseThrow();
        Product product = productRepository.findById(dto.getProductId()).orElseThrow();
        Deposit deposit = depositRepository.findById(dto.getDepositId()).orElseThrow();

        // Validate số lượng còn lại
        if (deposit.getRemainQuantity() <= 0)
            throw new FarmAppException("Đơn ký gửi đã hết hàng", HttpStatus.BAD_REQUEST);

        // Tạo Sell entity
        Sell sell = sellMapper.toEntity(dto, farmer, season, product, deposit);
        sell.setId(idGenerator.generateSellId());
        Float quantity = dto.getQuantityToSell();
        if (quantity == null || quantity <= 0) {
            throw new FarmAppException("Số lượng bán không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        if (deposit.getRemainQuantity() < quantity) {
            throw new FarmAppException("Không đủ hàng trong ký gửi để bán", HttpStatus.BAD_REQUEST);
        }

        sell.setTotalQuantity(quantity);
        sell.setAmountDue((int) Math.round(sell.getTotalQuantity() * sell.getPrice()));
        sell.setAmountPaid(0);
        sell.setPaymentStatus(PaymentStatus.UNPAID);
        deposit.setRemainQuantity(deposit.getRemainQuantity() - quantity);
        sell.setIsActive(true);
        depositRepository.save(deposit);
        sell = sellRepository.save(sell);
        // Phần tạo payment
        PaymentCreateSummaryRequestDTO payment = new PaymentCreateSummaryRequestDTO(dto.getFarmerId(),dto.getSeasonId(),dto.getProductId(), TransactionType.SELL_FROM_DEPOSIT,sell.getId());
        paymentService.createPaymentSummary(payment);

        return sellMapper.toResponseDTO(sell,
                farmer.getName(), season.getName(), product.getName());
    }

    @Override
    @Transactional
    public SellResponseDTO createSellDirect(SellCreateRequestDTO dto) {
        if (dto.getDepositId() != null)
            throw new FarmAppException("Không được chọn depositId cho giao dịch DIRECT", HttpStatus.BAD_REQUEST);

        User farmer = userRepository.findById(dto.getFarmerId()).orElseThrow();
        Season season = seasonRepository.findById(dto.getSeasonId()).orElseThrow();
        Product product = productRepository.findById(dto.getProductId()).orElseThrow();

        Sell sell = sellMapper.toEntity(dto, farmer, season, product, null);
        sell.setId(idGenerator.generateSellId());
        Float quantity = dto.getQuantityToSell();
        if (quantity == null || quantity <= 0) {
            throw new FarmAppException("Số lượng bán không hợp lệ", HttpStatus.BAD_REQUEST);
        }
        sell.setTotalQuantity(quantity); // Bổ sung dòng này
        sell.setAmountDue((int) Math.round(sell.getTotalQuantity() * sell.getPrice()));
        sell.setAmountPaid(0);
        sell.setPaymentStatus(PaymentStatus.UNPAID);
        sell = sellRepository.save(sell);
        // Phần tạo payment
        PaymentCreateSummaryRequestDTO payment = new PaymentCreateSummaryRequestDTO(dto.getFarmerId(),dto.getSeasonId(),dto.getProductId(), TransactionType.SELL_DIRECT,sell.getId());
        paymentService.createPaymentSummary(payment);
        return sellMapper.toResponseDTO(sell,
                farmer.getName(), season.getName(), product.getName());
    }

    @Override
    public SellResponseDTO updateSell(String id, SellUpdateRequestDTO dto) {
        Sell sell = sellRepository.findById(id).orElseThrow();
        sellMapper.updateEntity(sell, dto);
        sell = sellRepository.save(sell);
        return sellMapper.toResponseDTO(sell,
                sell.getFarmer().getName(),
                sell.getSeason().getName(),
                sell.getProduct().getName());
    }

    @Override
    @Transactional
    public void deleteSell(String id) {
        Sell sell = sellRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy đơn bán", HttpStatus.NOT_FOUND));

        if (!sell.getPaymentStatus().equals(PaymentStatus.UNPAID)) {
            throw new FarmAppException("Không thể xoá vi đã có phát sinh thanh toán", HttpStatus.BAD_REQUEST);
        }
        // Soft delete đơn bán
        sell.setIsActive(false);
        sellRepository.save(sell);
    }




    @Override
    public List<SellResponseDTO> getAllSellsInOnGoingSeasons() {
        return sellRepository.findAllInOnGoingSeasons().stream()
                .map(sell -> sellMapper.toResponseDTO(sell,
                        sell.getFarmer().getName(),
                        sell.getSeason().getName(),
                        sell.getProduct().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SellResponseDTO> filterSells(Integer farmerId, Integer seasonId, SellType sellType) {
        return sellRepository.filterSells(farmerId, seasonId, sellType).stream()
                .map(sell -> sellMapper.toResponseDTO(sell,
                        sell.getFarmer().getName(),
                        sell.getSeason().getName(),
                        sell.getProduct().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public SellResponseDTO getById(String id) {
        Sell sell = sellRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy đơn bán", HttpStatus.NOT_FOUND));
        return sellMapper.toResponseDTO(sell,
                sell.getFarmer().getName(),
                sell.getSeason().getName(),
                sell.getProduct().getName());
    }
}
