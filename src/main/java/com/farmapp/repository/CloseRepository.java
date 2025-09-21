package com.farmapp.repository;

import com.farmapp.model.Close;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloseRepository extends JpaRepository<Close, String> {

    /**
     * Filter danh sách Close theo nông dân, mùa vụ và sản phẩm.
     * Có thể truyền null ở bất kỳ tham số nào để bỏ qua điều kiện đó.
     *
     * @param farmerId   ID nông dân (nullable)
     * @param seasonId   ID mùa vụ (nullable)
     * @param productId  ID sản phẩm (nullable)
     * @return Danh sách Close thoả điều kiện lọc và đang hoạt động
     */
    @Query("""
        SELECT c FROM Close c
        WHERE (:farmerId IS NULL OR c.farmer.id = :farmerId)
          AND (:seasonId IS NULL OR c.season.id = :seasonId)
          AND (:productId IS NULL OR c.product.id = :productId)
          AND c.isActive = true
    """)
    List<Close> filterCloses(Integer farmerId, Integer seasonId, Integer productId);

    /**
     * Tìm Close theo ID và chỉ lấy nếu đang hoạt động (isActive = true)
     *
     * @param id ID của Close
     * @return Optional chứa Close nếu tồn tại và active
     */
    Optional<Close> findByIdAndIsActiveTrue(String id);

    /**
     * Lấy tất cả Close thuộc các mùa vụ đang hoạt động (status = ONGOING)
     * và isActive = true
     *
     * @return Danh sách Close thuộc các mùa vụ đang diễn ra
     */
    @Query("""
        SELECT c FROM Close c
        WHERE c.season.status = com.farmapp.enums.SeasonStatus.ONGOING
          AND c.isActive = true
    """)
    List<Close> findAllInOnGoingSeasons();

    /**
     * Kiểm tra xem Close có đang liên kết với Delivery nào không.
     * Dùng khi muốn xoá Close nhưng cần kiểm tra liên quan dữ liệu.
     *
     * @param id ID của Close
     * @return true nếu có ít nhất 1 Delivery liên kết
     */
    @Query("""
    SELECT COUNT(d) > 0 FROM Delivery d
    WHERE d.close.id = :closeId AND d.deliveredQuantity > 0
""")
    boolean existsAnyDeliveryWithQuantity(String closeId);

    List<Close> findByFarmerIdAndSeasonIdAndIsActiveIsTrue(Integer farmerId,Integer seasonId);
}

