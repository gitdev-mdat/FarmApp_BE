package com.farmapp.dto.response.Report;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReportTransactionResponseDTO {
    private List<EventDTO> events;
    private List<SummaryDTO> summary;
}
