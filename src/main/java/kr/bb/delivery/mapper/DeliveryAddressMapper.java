package kr.bb.delivery.mapper;

import bloomingblooms.domain.delivery.DeliveryAddressInsertDto;
import java.time.LocalDateTime;
import kr.bb.delivery.entity.DeliveryAddress;

public class DeliveryAddressMapper {
  public static DeliveryAddress toEntity(DeliveryAddressInsertDto dto) {
    return DeliveryAddress.builder()
        .userId(dto.getUserId())
        .deliveryRecipientName(dto.getRecipientName())
        .deliveryRoadName(dto.getRoadName())
        .deliveryAddressDetail(dto.getAddressDetail())
        .deliveryZipcode(dto.getZipcode())
        .deliveryRecipientPhoneNumber(dto.getPhoneNumber())
        .deliveryUsedAt(LocalDateTime.now())
        .build();
  }
}
