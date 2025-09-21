package com.farmapp.controller;

import com.farmapp.dto.request.Sell.SellCreateRequestDTO;
import com.farmapp.dto.request.Sell.SellUpdateRequestDTO;
import com.farmapp.dto.response.Sell.SellResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.enums.SellType;
import com.farmapp.service.interfaces.SellService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sells")
@RequiredArgsConstructor
public class SellController {

    private final SellService sellService;

    // 1. Tạo đơn bán từ ký gửi
    @PostMapping("/from-deposit")
    public ResponseEntity<GlobalResponse<SellResponseDTO>> createFromDeposit(@RequestBody SellCreateRequestDTO dto) {
        SellResponseDTO response = sellService.createSellFromDeposit(dto);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    // 2. Tạo đơn bán trực tiếp
    @PostMapping("/direct")
    public ResponseEntity<GlobalResponse<SellResponseDTO>> createDirect(@RequestBody SellCreateRequestDTO dto) {
        SellResponseDTO response = sellService.createSellDirect(dto);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    // 3. Cập nhật đơn bán
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<SellResponseDTO>> updateSell(@PathVariable String id, @RequestBody SellUpdateRequestDTO dto) {
        SellResponseDTO response = sellService.updateSell(id, dto);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    // 4. Xoá mềm đơn bán
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteSell(@PathVariable String id) {
        sellService.deleteSell(id);
        return ResponseEntity.ok(GlobalResponse.success(null));
    }

    // 5. Lấy tất cả đơn bán thuộc mùa vụ đang ONGOING
    @GetMapping
    public ResponseEntity<GlobalResponse<List<SellResponseDTO>>> getAllInOnGoingSeasons() {
        List<SellResponseDTO> list = sellService.getAllSellsInOnGoingSeasons();
        return ResponseEntity.ok(GlobalResponse.success(list));
    }

    // 6. Filter theo farmerId, seasonId, sellType (nullable)
    @GetMapping("/filter")
    public ResponseEntity<GlobalResponse<List<SellResponseDTO>>> filter(
            @RequestParam(required = false) Integer farmerId,
            @RequestParam(required = false) Integer seasonId,
            @RequestParam(required = false) SellType sellType
    ) {
        List<SellResponseDTO> list = sellService.filterSells(farmerId, seasonId, sellType);
        return ResponseEntity.ok(GlobalResponse.success(list));
    }

    // 7. Lấy chi tiết đơn bán theo ID
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<SellResponseDTO>> getById(@PathVariable String id) {
        SellResponseDTO response = sellService.getById(id);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }
}
