package kr.bb.delivery.dto.request;

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
    private Long deliveryAddressId;
    private Long userId;
    private String recipientName;
    private String zipcode;
    private String roadName;
    private String addressDetail;
    private String phoneNumber;

    public DeliveryAddress toEntity(){
        return DeliveryAddress.builder()
                .userId(this.getUserId())
                .deliveryRecipientName(this.getRecipientName())
                .deliveryRoadName(this.getRoadName())
                .deliveryAddressDetail(this.getAddressDetail())
                .deliveryZipcode(this.getZipcode())
                .deliveryRecipientPhoneNumber(this.getPhoneNumber())
                .build();
    }
}

