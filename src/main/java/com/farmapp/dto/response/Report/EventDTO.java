package com.farmapp.dto.response.Report;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class EventDTO {
    private String date;        // dd/MM/yyyy
    private String type;        // Ký gửi, Chốt giá, Bán hàng, ...
    private String description;
}
