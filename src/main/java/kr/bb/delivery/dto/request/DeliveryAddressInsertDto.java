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
public class DeliveryAddressInsertDto {
  @Nullable private Long deliveryAddressId;
  @NotEmpty private Long userId;
  @NotEmpty private String recipientName;
  @NotEmpty private String zipcode;
  @NotEmpty private String roadName;
  @NotEmpty private String addressDetail;
  @NotEmpty private String phoneNumber;
}
