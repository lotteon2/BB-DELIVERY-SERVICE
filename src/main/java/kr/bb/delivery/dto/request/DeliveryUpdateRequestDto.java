package kr.bb.delivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryUpdateRequestDto {
    private String recipientName;
    private String recipientPhoneNumber;
    private String zipcode;
    private String roadName;
    private String addressDetail;
}
