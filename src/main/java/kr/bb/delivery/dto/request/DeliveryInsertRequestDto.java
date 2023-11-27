package kr.bb.delivery.dto.request;

import kr.bb.delivery.entity.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryInsertRequestDto {
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

  public Delivery toEntity(){
    return Delivery.builder()
            .deliveryOrdererName(this.ordererName)
            .deliveryOrdererPhoneNumber(this.ordererPhoneNumber)
            .deliveryOrdererEmail(this.ordererEmail)
            .deliveryRecipientName(this.recipientName)
            .deliveryRecipientPhoneNumber(this.recipientPhoneNumber)
            .deliveryZipcode(this.zipcode)
            .deliveryRoadName(this.roadName)
            .deliveryAddressDetail(this.addressDetail)
            .deliveryRequest(this.request)
            .deliveryCost(this.deliveryCost)
            .build();
  }
}
