package kr.bb.delivery.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.dto.response.DeliveryReadResponseDto;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.exception.errors.DeliveryEntityNotFoundException;
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

    public List<DeliveryReadResponseDto> getDelivery(List<Long> deliveryIds){
        List<Delivery> deliveries = deliveryRepository.findAllById(deliveryIds);
        return deliveries.stream().map(DeliveryReadResponseDto::fromEntity).collect(Collectors.toList());
    }

}
