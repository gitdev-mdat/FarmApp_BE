package com.farmapp.controller;

import com.farmapp.dto.request.Deposit.DepositCreateRequestDTO;
import com.farmapp.dto.request.Deposit.DepositUpdateRequestDTO;
import com.farmapp.dto.response.Deposit.DepositResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.service.interfaces.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    // ✅ Tạo mới đơn ký gửi
    @PostMapping
    public ResponseEntity<GlobalResponse<DepositResponseDTO>> createDeposit(@RequestBody DepositCreateRequestDTO dto) {
        DepositResponseDTO created = depositService.createDeposit(dto);
        return ResponseEntity.ok(
                GlobalResponse.<DepositResponseDTO>builder()
                        .status(201)
                        .message("Tạo đơn ký gửi thành công")
                        .data(created)
                        .build()
        );
    }

    // ✅ Cập nhật đơn ký gửi (không cho sửa totalQuantity hay remainQuantity)
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<DepositResponseDTO>> updateDeposit(
            @PathVariable String id,
            @RequestBody DepositUpdateRequestDTO dto
    ) {
        DepositResponseDTO updated = depositService.updateDeposit(id, dto);
        return ResponseEntity.ok(
                GlobalResponse.<DepositResponseDTO>builder()
                        .status(200)
                        .message("Cập nhật đơn ký gửi thành công")
                        .data(updated)
                        .build()
        );
    }

    // ✅ Xoá mềm đơn ký gửi
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteDeposit(@PathVariable String id) {
        depositService.deleteDeposit(id);
        return ResponseEntity.ok(
                GlobalResponse.<String>builder()
                        .status(204)
                        .message("Xoá mềm đơn ký gửi thành công")
                        .build()
        );
    }

    // ✅ Lấy toàn bộ đơn ký gửi đang active
    @GetMapping
    public ResponseEntity<GlobalResponse<List<DepositResponseDTO>>> getAllDeposits() {
        List<DepositResponseDTO> deposits = depositService.getAllDeposits();
        return ResponseEntity.ok(
                GlobalResponse.<List<DepositResponseDTO>>builder()
                        .status(200)
                        .message("Lấy danh sách đơn ký gửi thành công")
                        .data(deposits)
                        .build()
        );
    }

    // ✅ Lọc theo farmerId và seasonId
    @GetMapping("/filter")
    public ResponseEntity<GlobalResponse<List<DepositResponseDTO>>> filterDeposits(
            @RequestParam(required = false) Integer farmerId,
            @RequestParam(required = false) List<Integer> seasonId
    ) {
        List<DepositResponseDTO> deposits = depositService.filterDeposits(farmerId, seasonId);
        return ResponseEntity.ok(
                GlobalResponse.<List<DepositResponseDTO>>builder()
                        .status(200)
                        .message("Lọc danh sách đơn ký gửi thành công")
                        .data(deposits)
                        .build()
        );
    }

    // ✅ Lấy chi tiết theo ID
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<DepositResponseDTO>> getDepositById(@PathVariable String id) {
        DepositResponseDTO dto = depositService.getById(id);
        return ResponseEntity.ok(
                GlobalResponse.<DepositResponseDTO>builder()
                        .status(200)
                        .message("Lấy chi tiết đơn ký gửi thành công")
                        .data(dto)
                        .build()
        );
    }
}
