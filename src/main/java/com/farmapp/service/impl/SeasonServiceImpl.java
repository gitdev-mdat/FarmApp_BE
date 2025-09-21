package com.farmapp.service.impl;

import com.farmapp.dto.request.Season.SeasonCreateRequestDTO;
import com.farmapp.dto.request.Season.SeasonUpdateRequestDTO;
import com.farmapp.dto.response.Season.SeasonResponseDTO;
import com.farmapp.enums.SeasonStatus;
import com.farmapp.mapper.SeasonMapper;
import com.farmapp.model.Season;
import com.farmapp.repository.SeasonRepository;
import com.farmapp.service.interfaces.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;
    private final SeasonMapper seasonMapper;

    @Override
    public List<SeasonResponseDTO> getAllSeasons() {
        return seasonRepository.findAllByIsActiveTrueOrderByCreatedAtDesc().stream()
                .map(seasonMapper::toResponseDTO)
                .toList();
    }

    @Override
    public SeasonResponseDTO getSeasonByName(String name) {
        Season season = seasonRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mùa vụ tên: " + name));
        return seasonMapper.toResponseDTO(season);
    }

    @Override
    public SeasonResponseDTO getSeasonById(int id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mùa vụ ID: " + id));
        return seasonMapper.toResponseDTO(season);
    }

    @Override
    public void deleteSeasonById(int id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mùa vụ ID: " + id));
        season.setIsActive(false); // giả định đang soft delete
        seasonRepository.save(season);
    }

    @Override
    public SeasonResponseDTO updateSeason(SeasonUpdateRequestDTO dto, int id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mùa vụ ID: " + id));

        if (dto.getEndDate() == null) {
            dto.setSeasonStatus(SeasonStatus.ONGOING);
        } else if (dto.getEndDate().isBefore(LocalDate.now())) {
            dto.setSeasonStatus(SeasonStatus.COMPLETED);
        } else {
            dto.setSeasonStatus(SeasonStatus.ONGOING);
        }

        seasonMapper.updateEntity(season, dto);
        return seasonMapper.toResponseDTO(seasonRepository.save(season));
    }


    @Override
    public void updateStatusSeason(int id, SeasonStatus status) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mùa vụ ID: " + id));
        season.setStatus(status);
        seasonRepository.save(season);
    }

    @Override
    public void createSeason(SeasonCreateRequestDTO dto) {
        Season season = seasonMapper.toEntity(dto);
        season.setStatus(SeasonStatus.ONGOING);
        season.setIsActive(true); // Nếu có cột này
        seasonRepository.save(season);
    }
}


