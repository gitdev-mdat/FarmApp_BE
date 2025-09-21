package com.farmapp.dto.response.Season;

import com.farmapp.enums.SeasonStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeasonResponseDTO {
    private Integer id;
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
    private SeasonStatus seasonStatus;
}
