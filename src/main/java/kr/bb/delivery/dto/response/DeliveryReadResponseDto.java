package kr.bb.delivery.dto.response;

import kr.bb.delivery.entity.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReadResponseDto {
  private Long deliveryId;
  private String orderTrackingNumber;
  private String ordererName;
  private String ordererPhoneNumber;
  private String ordererEmail;
  private String recipientName;
  private String recipientPhoneNumber;
  private String zipcode;
  private String roadName;
  private String addressDetail;
  private String request;
  private Long deliveryCost;
  private String deliveryStatus;

  public static DeliveryReadResponseDto fromEntity(Delivery delivery){
      return DeliveryReadResponseDto.builder()
            .deliveryId(delivery.getDeliveryId())
            .orderTrackingNumber(delivery.getDeliveryTrackingNumber())
            .ordererName(delivery.getDeliveryOrdererName())
            .ordererPhoneNumber(delivery.getDeliveryOrdererPhoneNumber())
            .ordererEmail(delivery.getDeliveryOrdererEmail())
            .recipientName(delivery.getDeliveryRecipientName())
            .recipientPhoneNumber(delivery.getDeliveryRecipientPhoneNumber())
            .zipcode(delivery.getDeliveryZipcode())
            .roadName(delivery.getDeliveryRoadName())
            .addressDetail(delivery.getDeliveryAddressDetail())
            .request(delivery.getDeliveryRequest())
            .deliveryCost(delivery.getDeliveryCost())
            .deliveryStatus(String.valueOf(delivery.getDeliveryStatus()))
            .build();
  }
}
