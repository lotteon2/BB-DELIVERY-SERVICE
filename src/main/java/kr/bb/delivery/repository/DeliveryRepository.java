package kr.bb.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;
import kr.bb.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Query("select d from Delivery d where d.deliveryId in :deliveryIds")
    List<Delivery> findAllByIds(List<Long> deliveryIds);

    @Query("select d from Delivery d where d.createdAt >= :startDateTime and d.createdAt < :endDateTime and d.deliveryStatus = 'PENDING'")
    List<Delivery> findAllByCreatedAtAndDeliveryStatus(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
