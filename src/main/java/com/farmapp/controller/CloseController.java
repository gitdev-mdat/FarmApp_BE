package com.farmapp.controller;

import com.farmapp.dto.request.Close.CloseCreateRequestDTO;
import com.farmapp.dto.request.Close.CloseUpdateRequestDTO;
import com.farmapp.dto.response.Close.CloseResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.service.interfaces.CloseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/closes")
@RequiredArgsConstructor
public class CloseController {

    private final CloseService closeService;

    // 1. Tạo mới Close
    @PostMapping
    public ResponseEntity<GlobalResponse<CloseResponseDTO>> createClose(
            @RequestBody CloseCreateRequestDTO dto
    ) {
        CloseResponseDTO result = closeService.createClose(dto);
        return ResponseEntity.ok(GlobalResponse.success(result));
    }

    // 2. Cập nhật Close
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<CloseResponseDTO>> updateClose(
            @PathVariable String id,
            @RequestBody CloseUpdateRequestDTO dto
    ) {
        CloseResponseDTO result = closeService.updateClose(id, dto);
        return ResponseEntity.ok(GlobalResponse.success(result));
    }

    // 3. Xoá mềm Close
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteClose(
            @PathVariable String id
    ) {
        closeService.deleteClose(id);
        return ResponseEntity.ok(GlobalResponse.success(null));
    }

    // 4. Lọc Close theo farmer, season, product (nullable params)
    @GetMapping("/filter")
    public ResponseEntity<GlobalResponse<List<CloseResponseDTO>>> filterCloses(
            @RequestParam(required = false) Integer farmerId,
            @RequestParam(required = false) Integer seasonId,
            @RequestParam(required = false) Integer productId
    ) {
        List<CloseResponseDTO> result = closeService.filterCloses(farmerId, seasonId, productId);
        return ResponseEntity.ok(GlobalResponse.success(result));
    }

    // 5. Lấy tất cả Close trong mùa vụ đang hoạt động
    @GetMapping
    public ResponseEntity<GlobalResponse<List<CloseResponseDTO>>> getAllCloses() {
        List<CloseResponseDTO> result = closeService.getAllCloses();
        return ResponseEntity.ok(GlobalResponse.success(result));
    }

    // 6. Lấy chi tiết Close theo ID
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<CloseResponseDTO>> getCloseById(
            @PathVariable String id
    ) {
        CloseResponseDTO result = closeService.getById(id);
        return ResponseEntity.ok(GlobalResponse.success(result));
    }
}
