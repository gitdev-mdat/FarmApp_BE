package com.farmapp.repository;

import com.farmapp.enums.SellType;
import com.farmapp.model.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellRepository extends JpaRepository<Sell, String> {

    // 1. Lấy danh sách Sell có mùa vụ đang ONGOING
    @Query("""
        SELECT s FROM Sell s
        WHERE s.season.status = com.farmapp.enums.SeasonStatus.ONGOING
        AND s.isActive = true
    """)
    List<Sell> findAllInOnGoingSeasons();

    // 2. Filter theo nông dân + mùa vụ + loại bán (bất kỳ field nào có thể null)
    @Query("""
        SELECT s FROM Sell s
        WHERE (:farmerId IS NULL OR s.farmer.id = :farmerId)
          AND (:seasonId IS NULL OR s.season.id = :seasonId)
          AND (:sellType IS NULL OR s.sellType = :sellType)
          AND s.isActive = true
    """)
    List<Sell> filterSells(Integer farmerId, Integer seasonId, SellType sellType);
    Optional<Sell> findByIdAndIsActiveTrue(String id);
    boolean existsByDepositId(String depositId);

   List<Sell> findBySellType(SellType sellType);
   List<Sell> findByFarmerIdAndSeasonIdAndIsActiveIsTrue(Integer farmerId,Integer seasonId);

}
