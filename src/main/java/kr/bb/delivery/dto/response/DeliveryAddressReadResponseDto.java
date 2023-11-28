package kr.bb.delivery.dto.response;

import kr.bb.delivery.entity.DeliveryAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddressReadResponseDto {
    private String recipientName;
    private String zipcode;
    private String roadName;
    private String addressDetail;
    private String phoneNumber;

    public static DeliveryAddressReadResponseDto fromEntity(DeliveryAddress deliveryAddress){
        return DeliveryAddressReadResponseDto.builder()
                .recipientName(deliveryAddress.getDeliveryRecipientName())
                .zipcode(deliveryAddress.getDeliveryZipcode())
                .roadName(deliveryAddress.getDeliveryRoadName())
                .addressDetail(deliveryAddress.getDeliveryAddressDetail())
                .phoneNumber(deliveryAddress.getDeliveryRecipientPhoneNumber())
                .build();
    }
}
