package kr.bb.delivery.repository;

import java.util.List;
import kr.bb.delivery.entity.DeliveryAddress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    List<DeliveryAddress> findAllByUserId(Long userId);
    Long countByUserId(Long userId);
    @Query("select da from DeliveryAddress da where da.userId = :userId order by da.createdAt desc")
    List<DeliveryAddress> findOldestByUserId(Long userId, Pageable pageable);
}
