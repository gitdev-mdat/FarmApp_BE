package com.farmapp.mapper;

import com.farmapp.dto.request.Season.SeasonCreateRequestDTO;
import com.farmapp.dto.request.Season.SeasonUpdateRequestDTO;
import com.farmapp.dto.response.Season.SeasonResponseDTO;
import com.farmapp.enums.SeasonStatus;
import com.farmapp.model.Season;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SeasonMapper {

    public Season toEntity(SeasonCreateRequestDTO dto) {
        return Season.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(SeasonStatus.ONGOING)
                .createdAt(LocalDate.now())
                .build();
    }

    public void updateEntity(Season season, SeasonUpdateRequestDTO dto) {
        season.setName(dto.getName());
        season.setStartDate(dto.getStartDate());
        season.setEndDate(dto.getEndDate());
        season.setStatus(dto.getSeasonStatus());
    }

    public SeasonResponseDTO toResponseDTO(Season season) {
        return SeasonResponseDTO.builder()
                .id(season.getId())
                .name(season.getName())
                .startDate(season.getStartDate())
                .endDate(season.getEndDate())
                .seasonStatus(season.getStatus())
                .build();
    }
}

