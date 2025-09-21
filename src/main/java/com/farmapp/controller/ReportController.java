package com.farmapp.controller;



import com.farmapp.dto.response.Report.ReportTransactionResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.service.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public GlobalResponse<ReportTransactionResponseDTO> getReport(@RequestParam Integer FarmerId, @RequestParam Integer SeasonId ) {
        ReportTransactionResponseDTO report = reportService.getTransactionReport(FarmerId, SeasonId);
        return GlobalResponse.success(report);
    }
}
