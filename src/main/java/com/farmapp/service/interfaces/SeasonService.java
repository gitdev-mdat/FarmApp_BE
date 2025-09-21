package com.farmapp.service.interfaces;

import com.farmapp.dto.request.Season.SeasonCreateRequestDTO;
import com.farmapp.dto.request.Season.SeasonUpdateRequestDTO;
import com.farmapp.dto.response.Season.SeasonResponseDTO;
import com.farmapp.enums.SeasonStatus;
import com.farmapp.model.Season;


import java.util.List;

public interface SeasonService  {
    List<SeasonResponseDTO> getAllSeasons();
    SeasonResponseDTO getSeasonByName(String name);
    SeasonResponseDTO getSeasonById(int id);
    void deleteSeasonById(int id);
    SeasonResponseDTO updateSeason(SeasonUpdateRequestDTO season, int id);
    void updateStatusSeason(int id, SeasonStatus status);
    void createSeason(SeasonCreateRequestDTO season);
}
