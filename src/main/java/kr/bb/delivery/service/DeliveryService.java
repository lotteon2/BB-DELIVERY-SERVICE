package kr.bb.delivery.service;

import bloomingblooms.domain.delivery.UpdateOrderStatusDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.bb.delivery.client.OrderServiceClient;
import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.dto.response.DeliveryReadResponseDto;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.entity.DeliveryStatus;
import kr.bb.delivery.exception.errors.DeliveryEntityNotFoundException;
import kr.bb.delivery.kafka.KafkaProducer;
import kr.bb.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {
  private final DeliveryRepository deliveryRepository;
  private final OrderServiceClient orderServiceClient;
  private final KafkaProducer<UpdateOrderStatusDto> orderStatusDtoKafkaProducer;

  @Transactional
  public List<Long> createDelivery(List<DeliveryInsertRequestDto> dtoList) {
    List<Long> deliveryIds = new ArrayList<>();
    for(int i=0; i<dtoList.size(); i++){
      Delivery delivery = dtoList.get(i).toEntity();
      deliveryIds.add(deliveryRepository.save(delivery).getDeliveryId());
    }
    return deliveryIds;
  }

  public List<DeliveryReadResponseDto> getDelivery(List<Long> deliveryIds) {
    List<Delivery> deliveries = deliveryRepository.findAllById(deliveryIds);
    return deliveries.stream()
        .map(DeliveryReadResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Transactional
  public Delivery updateDelivery(Long deliveryId, DeliveryUpdateRequestDto dto) {
    Delivery foundDelivery =
        deliveryRepository.findById(deliveryId).orElseThrow(DeliveryEntityNotFoundException::new);
    foundDelivery.modifyDeliveryInfo(dto);
    return deliveryRepository.save(foundDelivery);
  }

  @Transactional
  public Delivery changeStatus(String orderDeliveryId, String status) {
    // deliveryOrderId로 deliveryId 찾아내기.
    Long deliveryId = orderServiceClient.getDeliveryId(orderDeliveryId).getData();

    Delivery savedDelivery =
        deliveryRepository.findById(deliveryId).orElseThrow(DeliveryEntityNotFoundException::new);

    DeliveryStatus newStatus = DeliveryStatus.fromString(status);
    DeliveryStatus currentStatus = savedDelivery.getDeliveryStatus();

    if (newStatus.getOrder() < currentStatus.getOrder()) {
      throw new IllegalStateException("잘못된 요청입니다. 이전 배송 상태로 변경이 불가합니다.");
    }

    // 운송장번호 부여
    if(newStatus.equals(DeliveryStatus.PENDING)){
      savedDelivery.generateTrackingNumber(UUID.randomUUID().toString());
    }
    savedDelivery.modifyStatus(status);

    // order-service로 상태 sync 맞추기 kafka send
    UpdateOrderStatusDto updateOrderStatusDto = UpdateOrderStatusDto.builder()
            .orderDeliveryId(orderDeliveryId)
            .status(status)
            .build();
    orderStatusDtoKafkaProducer.send("order-delivery-status", updateOrderStatusDto);

    return deliveryRepository.save(savedDelivery);
  }
}
