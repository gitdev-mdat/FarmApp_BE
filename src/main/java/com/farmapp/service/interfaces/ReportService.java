package com.farmapp.service.interfaces;

import com.farmapp.dto.response.Report.ReportTransactionResponseDTO;

public interface ReportService {
    /**
     * Báo cáo tổng hợp giao dịch theo farmer và season.
     *
     * @param farmerId ID của nông dân
     * @param seasonId ID của mùa vụ
     * @return Báo cáo gồm danh sách sự kiện và tổng kết
     */
    ReportTransactionResponseDTO getTransactionReport(Integer farmerId, Integer seasonId);
}
