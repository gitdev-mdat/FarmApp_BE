    package com.farmapp.service.impl;

    import com.farmapp.dto.request.Deposit.DepositCreateRequestDTO;
    import com.farmapp.dto.request.Deposit.DepositUpdateRequestDTO;
    import com.farmapp.dto.response.Deposit.DepositResponseDTO;
    import com.farmapp.enums.SeasonStatus;
    import com.farmapp.exception.FarmAppException;
    import com.farmapp.mapper.DepositMapper;
    import com.farmapp.model.*;
    import com.farmapp.repository.*;
    import com.farmapp.helper.IdGenerator;
    import com.farmapp.service.interfaces.DepositService;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;

    import java.time.LocalDate;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class DepositServiceImpl implements DepositService {

        private final DepositRepository depositRepository;
        private final UserRepository userRepository;
        private final SeasonRepository seasonRepository;
        private final ProductRepository productRepository;
        private final IdGenerator idGenerator;
        private final DepositMapper depositMapper;
        private final SellRepository sellRepository;
        @Override
        public DepositResponseDTO createDeposit(DepositCreateRequestDTO dto) {
            User farmer = userRepository.findById(dto.getFarmerId())
                    .orElseThrow(() -> new FarmAppException("Farmer không tồn tại", HttpStatus.NOT_FOUND));
            Season season = seasonRepository.findById(dto.getSeasonId())
                    .orElseThrow(() -> new FarmAppException("Mùa vụ không tồn tại", HttpStatus.NOT_FOUND));
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new FarmAppException("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND));

            Deposit deposit = Deposit.builder()
                    .id(idGenerator.generateDepositId())
                    .farmer(farmer)
                    .season(season)
                    .product(product)
                    .totalQuantity(dto.getTotalQuantity())
                    .remainQuantity(dto.getTotalQuantity())
                    .note(dto.getNote())
                    .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDate.now())
                    .depositImageUrl(dto.getDepositImageUrl())
                    .isActive(true)
                    .build();

            deposit = depositRepository.save(deposit);
            return depositMapper.toResponseDTO(deposit, farmer.getName(), season.getName(), product.getName());
        }

    @Override
    public DepositResponseDTO updateDeposit(String id, DepositUpdateRequestDTO dto) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy đơn ký gửi", HttpStatus.NOT_FOUND));

        User farmer = userRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new FarmAppException("Farmer không tồn tại", HttpStatus.NOT_FOUND));
        Season season = seasonRepository.findById(dto.getSeasonId())
                .orElseThrow(() -> new FarmAppException("Mùa vụ không tồn tại", HttpStatus.NOT_FOUND));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new FarmAppException("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND));

        deposit.setFarmer(farmer);
        deposit.setSeason(season);
        deposit.setProduct(product);
        deposit.setNote(dto.getNote());
        deposit.setCreatedAt(dto.getCreatedAt());

        deposit = depositRepository.save(deposit);
        return depositMapper.toResponseDTO(deposit, farmer.getName(), season.getName(), product.getName());
    }

    @Override
    @Transactional
    public void deleteDeposit(String id) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy đơn ký gửi", HttpStatus.NOT_FOUND));

        if (sellRepository.existsByDepositId(deposit.getId())) {
            throw new FarmAppException("Không thể xoá vì đã được dùng để bán", HttpStatus.BAD_REQUEST);
        }

        deposit.setIsActive(false);
        depositRepository.save(deposit); // nếu lỗi ở đây mà không có @Transactional thì trạng thái sẽ không rollback
    }


    @Override
    public List<DepositResponseDTO> getAllDeposits() {
        List<Season> onGoingSeasons = seasonRepository.findByStatus(SeasonStatus.ONGOING);
        if (onGoingSeasons.isEmpty()) {
            throw new FarmAppException("Không có mùa vụ đang diễn ra", HttpStatus.NOT_FOUND);
        }
        List<Integer> seasonIds = onGoingSeasons.stream().map(Season::getId).toList();

        return depositRepository.filterDeposits(null, seasonIds).stream()
                .map(this::mapWithNames)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepositResponseDTO> filterDeposits(Integer farmerId, List<Integer> seasonId) {
        return depositRepository.filterDeposits(farmerId, seasonId).stream()
                .map(this::mapWithNames)
                .collect(Collectors.toList());
    }

    @Override
    public DepositResponseDTO getById(String id) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy đơn ký gửi", HttpStatus.NOT_FOUND));
        return mapWithNames(deposit);
    }

    private DepositResponseDTO mapWithNames(Deposit deposit) {
        String farmerName = userRepository.findById(deposit.getFarmer().getId())
                .orElseThrow(() -> new FarmAppException("Farmer không tồn tại", HttpStatus.NOT_FOUND))
                .getName();
        String seasonName = seasonRepository.findById(deposit.getSeason().getId())
                .orElseThrow(() -> new FarmAppException("Mùa vụ không tồn tại", HttpStatus.NOT_FOUND))
                .getName();
        String productName = productRepository.findById(deposit.getProduct().getId())
                .orElseThrow(() -> new FarmAppException("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND))
                .getName();

        return depositMapper.toResponseDTO(deposit, farmerName, seasonName, productName);
    }

}
