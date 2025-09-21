package com.farmapp.service.interfaces;

import com.farmapp.dto.request.Sell.SellCreateRequestDTO;
import com.farmapp.dto.request.Sell.SellUpdateRequestDTO;
import com.farmapp.dto.response.Sell.SellResponseDTO;
import com.farmapp.enums.SellType;

import java.util.List;

public interface SellService {

    // ✅ Create - bán từ ký gửi
    SellResponseDTO createSellFromDeposit(SellCreateRequestDTO dto);

    // ✅ Create - bán trực tiếp
    SellResponseDTO createSellDirect(SellCreateRequestDTO dto);

    // ✅ Update đơn bán (chỉ sửa giá, note, ngày)
    SellResponseDTO updateSell(String id, SellUpdateRequestDTO dto);

    // ✅ Soft delete (ẩn khỏi hệ thống)
    void deleteSell(String id);

    // ✅ Lấy danh sách đơn bán từ các mùa vụ ONGOING
    List<SellResponseDTO> getAllSellsInOnGoingSeasons();

    // ✅ Filter: theo farmerId, seasonId, sellType (nullable)
    List<SellResponseDTO> filterSells(Integer farmerId, Integer seasonId, SellType sellType);

    // ✅ Get by ID
    SellResponseDTO getById(String id);
}
