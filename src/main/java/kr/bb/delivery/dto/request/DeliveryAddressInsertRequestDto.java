package kr.bb.delivery.dto.request;

import java.time.LocalDateTime;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import kr.bb.delivery.entity.DeliveryAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressInsertRequestDto {
  @Nullable private Long deliveryAddressId;
  @NotEmpty private Long userId;
  @NotEmpty private String recipientName;
  @NotEmpty private String zipcode;
  @NotEmpty private String roadName;
  @NotEmpty private String addressDetail;
  @NotEmpty private String phoneNumber;

  public DeliveryAddress toEntity() {
    return DeliveryAddress.builder()
        .userId(this.getUserId())
        .deliveryRecipientName(this.getRecipientName())
        .deliveryRoadName(this.getRoadName())
        .deliveryAddressDetail(this.getAddressDetail())
        .deliveryZipcode(this.getZipcode())
        .deliveryRecipientPhoneNumber(this.getPhoneNumber())
        .deliveryUsedAt(LocalDateTime.now())
        .build();
  }
}
