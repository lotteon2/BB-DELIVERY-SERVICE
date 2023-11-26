package kr.bb.delivery.dto.request;

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
}
