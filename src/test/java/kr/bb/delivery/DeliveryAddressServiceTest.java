package kr.bb.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

import bloomingblooms.domain.delivery.DeliveryAddressInsertDto;
import java.util.List;
import kr.bb.delivery.dto.response.DeliveryAddressReadResponseDto;
import kr.bb.delivery.entity.DeliveryAddress;
import kr.bb.delivery.exception.errors.DeliveryAddressEntityNotFoundException;
import kr.bb.delivery.repository.DeliveryAddressRepository;
import kr.bb.delivery.service.DeliveryAddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
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
    assertEquals(deliveryAddressList.size(), 3);
    assertThat(deliveryAddressList)
        .extracting("recipientName")
        .contains( "홍길동", "손흥민");
  }

  @Test
  @DisplayName("기존 배송지 선택")
  void selectExistingDeliveryAddressFromList() {
    DeliveryAddressInsertDto requestDto = createDeliveryAddressInsertRequest(1L, "손흥민", 5L);

    deliveryAddressService.createDeliveryAddress(requestDto);
    List<DeliveryAddress> list = deliveryAddressRepository.findAllByUserId(1L);

    assertEquals(list.size(), 1);
    assertThat(list).extracting("userId", "deliveryRecipientName").contains(tuple(1L, "홍길동"));
  }

  @Test
  @DisplayName("신규 배송지 생성")
  void appendDeliveryAddressToList() {
    DeliveryAddressInsertDto requestDto = createDeliveryAddressInsertRequest(1L, "손흥민", null);
  
    deliveryAddressService.createDeliveryAddress(requestDto);
    List<DeliveryAddress> list = deliveryAddressRepository.findAllByUserId(1L);
  
    assertEquals(list.size(), 2);
    assertThat(list).extracting("userId", "deliveryRecipientName").contains(tuple(1L, "손흥민"));
  }

  @Test
  @DisplayName("배송지 목록이 10개이면 신규 배송지를 가장 오래된 배송지에 덮어쓴다")
  void addressListShouldNotExceedTheLimit(){
    // given
    // 가장 오래된 배송지 entity 먼저 찾아놓기
    DeliveryAddress oldestDeliveryAddress = deliveryAddressRepository.findOldestByUserId(5L, Pageable.ofSize(1)).stream().findFirst().orElseThrow(
            DeliveryAddressEntityNotFoundException::new);

    // when
    DeliveryAddressInsertDto requestDto = createDeliveryAddressInsertRequest(5L, "수신자11", null);
    deliveryAddressService.createDeliveryAddress(requestDto);

    // then
    // 위 entity가 새로운 배송지로 덮어쓰기가 됐다
    assertEquals(deliveryAddressRepository.countByUserId(5L), 10);
    assertThat(oldestDeliveryAddress.getDeliveryRecipientName()).isEqualTo("수신자11");
  }

  DeliveryAddressInsertDto createDeliveryAddressInsertRequest(Long userId, String recipientName, Long deliveryAddressId){
    return DeliveryAddressInsertDto.builder()
            .deliveryAddressId(deliveryAddressId)
            .userId(userId)
            .recipientName(recipientName)
            .zipcode("우편번호")
            .roadName("도로명")
            .addressDetail("상세주소")
            .phoneNumber("수신자 연락처")
            .build();
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
