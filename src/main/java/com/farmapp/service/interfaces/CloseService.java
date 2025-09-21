package com.farmapp.service.interfaces;

import com.farmapp.dto.request.Close.CloseCreateRequestDTO;
import com.farmapp.dto.request.Close.CloseUpdateRequestDTO;
import com.farmapp.dto.response.Close.CloseResponseDTO;

import java.util.List;

public interface CloseService {

    /**
     * Tạo mới giao dịch chốt đơn (Close).
     *
     * @param dto dữ liệu tạo Close
     * @return CloseResponseDTO sau khi tạo
     */
    CloseResponseDTO createClose(CloseCreateRequestDTO dto);

    /**
     * Cập nhật thông tin giao dịch Close theo ID.
     *
     * @param id  ID của Close cần cập nhật
     * @param dto dữ liệu cập nhật
     * @return CloseResponseDTO sau khi cập nhật
     */
    CloseResponseDTO updateClose(String id, CloseUpdateRequestDTO dto);

    /**
     * Xoá mềm (set isActive = false) một Close theo ID.
     * Có thể kèm theo điều kiện không xoá nếu đã liên kết với Delivery.
     *
     * @param id ID của Close cần xoá
     */
    void deleteClose(String id);

    /**
     * Lấy danh sách tất cả Close thuộc mùa vụ đang diễn ra.
     *
     * @return danh sách CloseResponseDTO
     */
    List<CloseResponseDTO> getAllCloses();

    /**
     * Lọc Close theo nông dân, mùa vụ, sản phẩm.
     * Các tham số lọc có thể null.
     *
     * @param farmerId  ID nông dân (nullable)
     * @param seasonId  ID mùa vụ (nullable)
     * @param productId ID sản phẩm (nullable)
     * @return danh sách Close phù hợp
     */
    List<CloseResponseDTO> filterCloses(Integer farmerId, Integer seasonId, Integer productId);

    /**
     * Lấy chi tiết 1 Close theo ID.
     *
     * @param id ID của Close
     * @return CloseResponseDTO
     */
    CloseResponseDTO getById(String id);
}
