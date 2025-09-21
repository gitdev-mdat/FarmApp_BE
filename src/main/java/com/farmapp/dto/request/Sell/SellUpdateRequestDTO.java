package com.farmapp.dto.request.Sell;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellUpdateRequestDTO {
    private Integer price;         // Có thể cập nhật giá nếu nhập sai
    private String note;           // Chỉnh sửa ghi chú (nếu cần)
    private LocalDate createdAt;   // Có thể chỉnh ngày giao dịch
}
