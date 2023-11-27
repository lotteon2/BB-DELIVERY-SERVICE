package kr.bb.delivery.service;

import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery createDelivery(DeliveryInsertRequestDto dto){
        Delivery delivery = dto.toEntity();
        return deliveryRepository.save(delivery);
    }

}
