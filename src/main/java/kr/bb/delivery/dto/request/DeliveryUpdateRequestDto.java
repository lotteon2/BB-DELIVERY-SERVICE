package kr.bb.delivery.dto.request;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryUpdateRequestDto {
    @NotEmpty private String recipientName;
    @NotEmpty private String recipientPhoneNumber;
    @NotEmpty private String zipcode;
    @NotEmpty private String roadName;
    @NotEmpty private String addressDetail;
}
