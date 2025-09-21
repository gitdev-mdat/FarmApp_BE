package com.farmapp.repository;

import com.farmapp.enums.SeasonStatus;
import com.farmapp.enums.TransactionType;
import com.farmapp.model.Payment;
import com.farmapp.model.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    /**
     * 1. Lấy tất cả payment liên quan đến 1 transaction cụ thể (Close hoặc Sell)
     */
    // Danh cho get
    List<Payment> findAllByTransactionTypeAndTransactionIdAndIsActiveTrueAndPaymentDateNotNull(TransactionType transactionType, String transactionId);
    // Dànd cho delete
    List<Payment> findAllByTransactionTypeAndTransactionIdAndIsActiveTrue(TransactionType transactionType, String transactionId);
    /**
     * 2. Lọc danh sách payment theo farmerId, seasonId, productId
     * Nếu muốn truyền nullable → cần viết thêm @Query sau này, còn giờ để đơn giản vậy trước
     */
    List<Payment> findByFarmerIdAndSeasonIdAndProductId(Integer farmerId, Integer seasonId, Integer productId);

    Optional<Payment> findByIdAndIsActiveTrue(Integer paymentId);

    // 3. Lấy danh sách Sell có mùa vụ đang ONGOING
    @Query("""
    SELECT p FROM Payment p
    WHERE p.isActive = true
      AND (:farmerId IS NULL OR p.farmer.id = :farmerId)
      AND (:productId IS NULL OR p.product.id = :productId)
      AND p.season.status = 'ONGOING'
""")
    List<Payment> findAllInOnGoingSeasons(
            @Param("farmerId") Integer farmerId,
            @Param("productId") Integer productId
    );
    List<Payment> findPaymentByTransactionIdAndIsActiveTrue( String transactionId);

    List<Payment> findByFarmerIdAndSeasonIdAndIsActiveIsTrueAndPaymentDateNotNull(Integer farmerId, Integer seasonId);


}
