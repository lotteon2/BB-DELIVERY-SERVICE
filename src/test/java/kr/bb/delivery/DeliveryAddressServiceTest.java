package kr.bb.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import kr.bb.delivery.dto.response.DeliveryAddressReadResponseDto;
import kr.bb.delivery.entity.DeliveryAddress;
import kr.bb.delivery.repository.DeliveryAddressRepository;
import kr.bb.delivery.service.DeliveryAddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DeliveryAddressServiceTest {
  @Autowired private DeliveryAddressService deliveryAddressService;
  @Autowired private DeliveryAddressRepository deliveryAddressRepository;

  @Test
  @DisplayName("배송 주소지 목록 불러오기")
  void readDeliveryAddressList() {
    // given
    DeliveryAddress deliveryAddress1 = createDeliveryAddressEntity(1L, "홍길동");
    DeliveryAddress deliveryAddress2 = createDeliveryAddressEntity(1L, "손흥민");
    deliveryAddressRepository.saveAll(List.of(deliveryAddress1, deliveryAddress2));

    // when
    List<DeliveryAddressReadResponseDto> deliveryAddressList =
        deliveryAddressService.getDeliveryAddress(1L);

    // then
    assertEquals(deliveryAddressList.size(), 2);
    assertThat(deliveryAddressList)
        .extracting("recipientName")
        .contains( "홍길동", "손흥민");
  }

  DeliveryAddress createDeliveryAddressEntity(Long userId, String recipientName) {
    return DeliveryAddress.builder()
        .userId(userId)
        .deliveryRecipientName(recipientName)
        .deliveryRoadName("도로명")
        .deliveryAddressDetail("도로명 상세 주소")
        .deliveryZipcode("우편번호")
        .deliveryRecipientPhoneNumber("수신자 연락처")
        .build();
  }
}
