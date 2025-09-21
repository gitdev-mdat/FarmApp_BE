package com.farmapp.dto.request.Sell;

import com.farmapp.enums.SellType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellCreateRequestDTO {
    private Integer farmerId;
    private Integer seasonId;
    private Integer productId;
    private String depositId; // null nếu bán trực tiếp
    private Float quantityToSell;
    private Integer price;
    private String note;
    private LocalDate createdAt;
    private String sellImageUrl;
}
