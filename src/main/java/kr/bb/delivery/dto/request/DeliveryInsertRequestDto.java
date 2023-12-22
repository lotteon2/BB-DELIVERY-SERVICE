package kr.bb.delivery.dto.request;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
  @NotEmpty private String ordererName;
  @NotEmpty private String ordererPhoneNumber;
  @NotEmpty private String ordererEmail;
  @NotEmpty private String recipientName;
  @NotEmpty private String recipientPhoneNumber;
  @NotEmpty private String zipcode;
  @NotEmpty private String roadName;
  @NotEmpty private String addressDetail;
  @Nullable private String request;
  @NotNull private Long deliveryCost;

  public Delivery toEntity() {
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
