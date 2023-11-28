package kr.bb.delivery.repository;

import java.util.Optional;
import kr.bb.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
