package com.farmapp.service.interfaces;

import com.farmapp.dto.request.Delivery.DeliveryCreateRequestDTO;
import com.farmapp.dto.request.Delivery.DeliveryUpdateRequestDTO;
import com.farmapp.dto.response.Delivery.DeliveryDetailResponseDTO;
import com.farmapp.dto.response.Delivery.DeliverySummaryResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;

import java.util.List;

public interface DeliveryService {

    // Tạo mới 1 lần giao hàng cho close
    void createDelivery(DeliveryCreateRequestDTO dto);

    // Cập nhật lại 1 lần giao hàng (sửa số lượng, note, ngày)
    void updateDelivery(Integer deliveryId, DeliveryUpdateRequestDTO dto);

    // Lấy danh sách các lần giao của 1 Close (lịch sử)
    List<DeliveryDetailResponseDTO> getDeliveryHistoryByCloseId(String closeId);

    // Trả về tổng hợp trạng thái giao cho 1 Close (tổng đã giao, còn thiếu, status,...)
    DeliverySummaryResponseDTO getDeliverySummaryByCloseId(String closeId);

    void deleteDelivery(Integer deliveryId);

    List<DeliverySummaryResponseDTO> getAllDeliverySummary();
}
