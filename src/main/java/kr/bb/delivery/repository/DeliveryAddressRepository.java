package kr.bb.delivery.repository;

import java.util.List;
import kr.bb.delivery.entity.DeliveryAddress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    @Query("SELECT da FROM DeliveryAddress da WHERE da.userId = :userId ORDER BY da.deliveryUsedAt DESC")
    List<DeliveryAddress> findAllByUserId(Long userId);
    Long countByUserId(Long userId);
    @Query("select da from DeliveryAddress da where da.userId = :userId order by da.deliveryUsedAt ASC")
    List<DeliveryAddress> findOldestByUserId(Long userId, Pageable pageable);
}
