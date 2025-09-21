package com.farmapp.service.interfaces;

import com.farmapp.dto.request.Deposit.DepositCreateRequestDTO;
import com.farmapp.dto.request.Deposit.DepositUpdateRequestDTO;
import com.farmapp.dto.response.Deposit.DepositResponseDTO;

import java.util.List;

public interface DepositService {

    // ✅ Create: Tự fetch entity liên quan để mapping response
    DepositResponseDTO createDeposit(DepositCreateRequestDTO dto);

    // ✅ Update: Chỉ update note + createdAt, fetch name để trả ra
    DepositResponseDTO updateDeposit(String id, DepositUpdateRequestDTO dto);

    // ✅ Soft delete (update isDeleted = true)
    void deleteDeposit(String id);

    // ✅ Get all (chỉ lấy deposit thuộc các mùa ON_GOING)
    // BE sẽ tự fetch name (farmer, product, season) để build response
    List<DepositResponseDTO> getAllDeposits();

    // ✅ Filter theo farmerId + seasonIds (nullable)
    List<DepositResponseDTO> filterDeposits(Integer farmerId, List<Integer> seasonId);

    // ✅ Get by ID (trả kèm tên nông dân, mùa vụ, sản phẩm)
    DepositResponseDTO getById(String id);
}

