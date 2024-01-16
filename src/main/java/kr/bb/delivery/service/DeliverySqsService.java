package kr.bb.delivery.service;

import bloomingblooms.domain.delivery.UpdateOrderSubscriptionStatusDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.kafka.KafkaProducer;
import kr.bb.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliverySqsService {
    private final DeliveryRepository deliveryRepository;
    private final KafkaProducer<UpdateOrderSubscriptionStatusDto> orderSubscriptionStatusDtoKafkaProducer;

    @Transactional
    public void updateSubscriptionDelivery(LocalDateTime now) {
        LocalDateTime startDateTime = now.minusDays(3).toLocalDate().atStartOfDay();
        LocalDateTime endDateTime = now.minusDays(2).toLocalDate().atStartOfDay();

        List<Delivery> allByCreatedAtAndDeliveryStatus = deliveryRepository.findAllByCreatedAtAndDeliveryStatus(
                startDateTime, endDateTime);

        List<Long> deliveryIds = allByCreatedAtAndDeliveryStatus.stream()
                .map(Delivery::getDeliveryId)
                .collect(Collectors.toList());

        // order-service로 kafka 통신
        UpdateOrderSubscriptionStatusDto updateOrderSubscriptionStatusDto = UpdateOrderSubscriptionStatusDto.builder()
                .deliveryIds(deliveryIds)
                .build();
        orderSubscriptionStatusDtoKafkaProducer.send("order-subscription-status", updateOrderSubscriptionStatusDto);
    }
}
