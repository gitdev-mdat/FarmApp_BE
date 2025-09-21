package com.farmapp.dto.request.Season;

import com.farmapp.enums.SeasonStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeasonUpdateRequestDTO {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private SeasonStatus seasonStatus;
}
