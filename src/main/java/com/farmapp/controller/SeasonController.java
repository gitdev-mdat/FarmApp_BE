package com.farmapp.controller;

import com.farmapp.dto.request.Season.SeasonCreateRequestDTO;
import com.farmapp.dto.request.Season.SeasonUpdateRequestDTO;
import com.farmapp.dto.response.Season.SeasonResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.enums.SeasonStatus;
import com.farmapp.service.interfaces.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/seasons")
@RequiredArgsConstructor
public class SeasonController {
    private final SeasonService seasonService;
    @GetMapping
    public ResponseEntity<GlobalResponse<List<SeasonResponseDTO>>> getAllSeasons() {
        List<SeasonResponseDTO> seasons = seasonService.getAllSeasons();
        return ResponseEntity.ok(
                GlobalResponse.<List<SeasonResponseDTO>>builder()
                        .status(200)
                        .message("Lấy danh sách mùa vụ thành công")
                        .data(seasons)
                        .build()
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteSeason(@PathVariable Integer id) {
        seasonService.deleteSeasonById(id); // nếu ID không tồn tại, trong service đã throw lỗi rồi

        return ResponseEntity.ok(
                GlobalResponse.<Void>builder()
                        .status(200)
                        .message("Xoá (ẩn) mùa vụ thành công")
                        .build()
        );
    }

    @GetMapping("/name")
    public ResponseEntity<GlobalResponse<SeasonResponseDTO>> getSeasonByName(@RequestParam("value") String name) {
        SeasonResponseDTO season = seasonService.getSeasonByName(name);
        return ResponseEntity.ok(
                GlobalResponse.<SeasonResponseDTO>builder()
                        .status(200)
                        .message("Lấy mùa vụ theo tên thành công")
                        .data(season)
                        .build()
        );
    }

    // [2] PATCH /api/seasons/{id}/status?value=IN_PROGRESS
    @PatchMapping("/{id}/status")
    public ResponseEntity<GlobalResponse<Void>> updateSeasonStatus(
            @PathVariable Integer id,
            @RequestParam("value") SeasonStatus status
    ) {
        seasonService.updateStatusSeason(id, status);
        return ResponseEntity.ok(
                GlobalResponse.<Void>builder()
                        .status(200)
                        .message("Cập nhật trạng thái mùa vụ thành công")
                        .build()
        );
    }

    // [3] PUT /api/seasons/{id}
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<SeasonResponseDTO>> updateSeason(
            @PathVariable Integer id,
            @RequestBody SeasonUpdateRequestDTO dto
    ) {
        SeasonResponseDTO updatedSeason = seasonService.updateSeason(dto, id);
        return ResponseEntity.ok(
                GlobalResponse.<SeasonResponseDTO>builder()
                        .status(200)
                        .message("Cập nhật mùa vụ thành công")
                        .data(updatedSeason)
                        .build()
        );
    }


    @PostMapping
    public ResponseEntity<GlobalResponse<Void>> createSeason(@RequestBody SeasonCreateRequestDTO dto) {
        seasonService.createSeason(dto);
        return ResponseEntity.ok(GlobalResponse.success(null, "Tạo mùa vụ thành công"));
    }

}
