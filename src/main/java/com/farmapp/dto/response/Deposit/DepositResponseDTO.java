package com.farmapp.dto.response.Deposit;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositResponseDTO {
    private String id;
    private Integer farmerId;
    private String farmerName;
    private Integer seasonId;
    private String seasonName;
    private Integer productId;
    private String productName;
    private Float totalQuantity;
    private Float soldQuantity;
    private Float remainQuantity;
    private Float soldProgress;
    private String note;
    private LocalDate createdAt;
    private String depositImageUrl;
    // Không trả về isActive vì repo đã filter rồi
}
