package com.farmapp.dto.response.Report;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class SummaryDTO {
    private String label;       // Ví dụ: "Tổng ký gửi"
    private String value;       // "100kg", "2.500.000đ"
    private String status;
}
