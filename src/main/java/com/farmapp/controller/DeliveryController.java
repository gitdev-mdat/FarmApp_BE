package com.farmapp.controller;

import com.farmapp.dto.request.Delivery.DeliveryCreateRequestDTO;
import com.farmapp.dto.request.Delivery.DeliveryUpdateRequestDTO;
import com.farmapp.dto.response.Delivery.DeliveryDetailResponseDTO;
import com.farmapp.dto.response.Delivery.DeliverySummaryResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.service.interfaces.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<GlobalResponse<Void>> createDelivery(@RequestBody DeliveryCreateRequestDTO dto) {
        deliveryService.createDelivery(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(null));
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<GlobalResponse<Void>> updateDelivery(
            @PathVariable Integer deliveryId,
            @RequestBody DeliveryUpdateRequestDTO dto) {

        deliveryService.updateDelivery(deliveryId, dto);
        return ResponseEntity.ok(GlobalResponse.success(null));
    }

    @GetMapping("/close/{closeId}/history")
    public ResponseEntity<GlobalResponse<List<DeliveryDetailResponseDTO>>> getDeliveryHistory(
            @PathVariable String closeId) {

        List<DeliveryDetailResponseDTO> history = deliveryService.getDeliveryHistoryByCloseId(closeId);
        return ResponseEntity.ok(GlobalResponse.success(history));
    }

    @GetMapping("/close/{closeId}/summary")
    public ResponseEntity<GlobalResponse<DeliverySummaryResponseDTO>> getDeliverySummary(
            @PathVariable String closeId) {

        DeliverySummaryResponseDTO summary = deliveryService.getDeliverySummaryByCloseId(closeId);
        return ResponseEntity.ok(GlobalResponse.success(summary));
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<GlobalResponse<Void>> deleteDelivery(@PathVariable Integer deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.ok(GlobalResponse.success(null));
    }
    @GetMapping("/summary")
    public ResponseEntity<GlobalResponse<List<DeliverySummaryResponseDTO>>> getAllSummary() {
        List<DeliverySummaryResponseDTO> all = deliveryService.getAllDeliverySummary();
        return ResponseEntity.ok(GlobalResponse.success(all));
    }

}
