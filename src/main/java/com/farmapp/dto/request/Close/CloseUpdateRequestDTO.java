package com.farmapp.dto.request.Close;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CloseUpdateRequestDTO {
    private Float totalQuantity;
    private Integer price;
    private String note;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;
}
