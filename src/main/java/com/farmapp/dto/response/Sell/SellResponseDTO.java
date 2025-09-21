package com.farmapp.dto.response.Sell;

import com.farmapp.enums.SellType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellResponseDTO {
    private String id;
    private Integer farmerId;
    private String farmerName;
    private Integer seasonId;
    private String seasonName;
    private Integer productId;
    private String productName;
    private Integer price;
    private Float totalQuantity;
    private String depositId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;
    private SellType sellType;
    private String sellImageUrl;
}
