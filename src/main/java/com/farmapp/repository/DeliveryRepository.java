package com.farmapp.repository;

import com.farmapp.enums.DeliveryStatus;
import com.farmapp.model.Delivery;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    List<Delivery> findAllByClose_Id(String closeId);
    List<Delivery> findAllByFarmer_IdAndSeason_IdAndStatus(Integer farmer_id, Integer season_id,  @Nullable  DeliveryStatus status);
    List<Delivery> findAllByFarmer_Id(Integer farmer_id);
    List<Delivery> findAllBySeason_Id(Integer season_id);
    List<Delivery> findAllByStatus(DeliveryStatus status);
    @Query("SELECT SUM(d.deliveredQuantity) FROM Delivery d WHERE d.close.id = :closeId")
    Float sumDeliveredQuantityByCloseId(String closeId);
    Optional<Delivery> findByIdAndIsActiveTrue(Integer id);
    @EntityGraph(attributePaths = {"farmer", "season", "product", "close"})
    List<Delivery> findAllByIsActiveTrue();
    List<Delivery> findByFarmerIdAndSeasonIdAndIsActiveIsTrueAndDeliveredAtNotNull(Integer farmer_id, Integer season_id);
}
