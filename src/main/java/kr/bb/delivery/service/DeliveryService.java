package kr.bb.delivery.service;

import bloomingblooms.domain.delivery.DeliveryInfoDto;
import bloomingblooms.domain.delivery.UpdateOrderStatusDto;
import bloomingblooms.domain.notification.delivery.DeliveryStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kr.bb.delivery.client.OrderServiceClient;
import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.dto.response.DeliveryReadResponseDto;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.exception.errors.DeliveryEntityNotFoundException;
import kr.bb.delivery.kafka.KafkaProducer;
import kr.bb.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {
  private final DeliveryRepository deliveryRepository;
  private final OrderServiceClient orderServiceClient;
  private final KafkaProducer<UpdateOrderStatusDto> orderStatusDtoKafkaProducer;
  private DeliveryService deliveryService;

  @Autowired
  public void setDeliveryService(DeliveryService deliveryService) {
    this.deliveryService = deliveryService;
  }

  @Transactional
  public List<Long> createDelivery(List<DeliveryInsertRequestDto> dtoList) {
    List<Long> deliveryIds = new ArrayList<>();
    for (int i = 0; i < dtoList.size(); i++) {
      Delivery delivery = dtoList.get(i).toEntity();
      delivery.generateTrackingNumber(UUID.randomUUID().toString());
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
    return foundDelivery;
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

    savedDelivery.modifyStatus(newStatus);

    // order-service로 상태 sync 맞추기 kafka send
    UpdateOrderStatusDto updateOrderStatusDto =
        UpdateOrderStatusDto.builder().orderDeliveryId(orderDeliveryId).deliveryStatus(newStatus).build();
    orderStatusDtoKafkaProducer.send("order-delivery-status", updateOrderStatusDto);

    return deliveryRepository.save(savedDelivery);
  }

  @Transactional
  public List<Long> createDeliveryForSubscription(List<Long> oldDeliveryIds) {
    List<Long> newDeliveryIds = new ArrayList<>();
    for (Long oldDeliveryId : oldDeliveryIds) {
      Delivery delivery =
          deliveryRepository.findById(oldDeliveryId).orElseThrow(EntityNotFoundException::new);
      Delivery newDelivery = deliveryRepository.save(createNewDelivery(delivery));
      newDeliveryIds.add(newDelivery.getDeliveryId());
    }
    return newDeliveryIds;
  }

  private Delivery createNewDelivery(Delivery delivery) {
    return Delivery.builder()
        .deliveryOrdererName(delivery.getDeliveryOrdererName())
        .deliveryOrdererPhoneNumber(delivery.getDeliveryOrdererPhoneNumber())
        .deliveryOrdererEmail(delivery.getDeliveryOrdererEmail())
        .deliveryRecipientName(delivery.getDeliveryRecipientName())
        .deliveryRoadName(delivery.getDeliveryRoadName())
        .deliveryAddressDetail(delivery.getDeliveryAddressDetail())
        .deliveryZipcode(delivery.getDeliveryZipcode())
        .deliveryRecipientPhoneNumber(delivery.getDeliveryRecipientPhoneNumber())
        .deliveryRequest(delivery.getDeliveryRequest())
        .deliveryCost(delivery.getDeliveryCost())
        .deliveryStatus(DeliveryStatus.PENDING)
        .build();
  }

  public Map<Long, DeliveryInfoDto> getDeliveryInfo(List<Long> deliveryIds) {
    List<Delivery> allDeliveryByIds = deliveryRepository.findAllByIds(deliveryIds);
    return allDeliveryByIds.stream()
        .collect(
            Collectors.toMap(
                Delivery::getDeliveryId,
                delivery ->
                    DeliveryInfoDto.builder()
                        .ordererName(delivery.getDeliveryOrdererName())
                        .ordererPhone(delivery.getDeliveryOrdererPhoneNumber())
                        .ordererEmail(delivery.getDeliveryOrdererEmail())
                        .recipientName(delivery.getDeliveryRecipientName())
                        .recipientPhone(delivery.getDeliveryRecipientPhoneNumber())
                        .zipcode(delivery.getDeliveryZipcode())
                        .roadName(delivery.getDeliveryRoadName())
                        .addressDetail(delivery.getDeliveryAddressDetail())
                        .deliveryRequest(delivery.getDeliveryRequest())
                        .deliveryCost(delivery.getDeliveryCost())
                        .build()));
  }
}
