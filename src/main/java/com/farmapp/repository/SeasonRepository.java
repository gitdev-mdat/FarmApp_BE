package com.farmapp.repository;

import com.farmapp.dto.response.Season.SeasonResponseDTO;
import com.farmapp.enums.SeasonStatus;
import com.farmapp.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {
    @Override
    Optional<Season> findById(Integer id);

    Optional<Season> findByName(String name);

    List<Season> findByStatus(SeasonStatus status);
    List<Season> findAllByIsActiveTrueOrderByCreatedAtDesc();
}
