package com.farmapp.repository;

import com.farmapp.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, String>{
    @Query("""
    SELECT d FROM Deposit d 
    WHERE d.isActive = true
      AND (:farmerId IS NULL OR d.farmer.id = :farmerId)
      AND (:seasonIds IS NULL OR d.season.id IN :seasonIds)
    ORDER BY d.createdAt DESC
""")
    List<Deposit> filterDeposits(@Param("farmerId") Integer farmerId,
                                 @Param("seasonIds") List<Integer> seasonIds);

    List<Deposit> findByFarmerIdAndSeasonIdAndIsActiveIsTrue(Integer farmerId, Integer seasonId);
}
