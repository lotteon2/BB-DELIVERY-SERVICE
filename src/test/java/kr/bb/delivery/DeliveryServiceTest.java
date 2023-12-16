package kr.bb.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import bloomingblooms.response.SuccessResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.bb.delivery.client.OrderServiceClient;
import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.dto.response.DeliveryReadResponseDto;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.entity.DeliveryStatus;
import kr.bb.delivery.repository.DeliveryRepository;
import kr.bb.delivery.service.DeliveryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DeliveryServiceTest {
  @Autowired private DeliveryService deliveryService;
  @Autowired private DeliveryRepository deliveryRepository;
  @MockBean private OrderServiceClient orderServiceClient;

  @BeforeEach
  void setup() {
    deliveryService = new DeliveryService(deliveryRepository, orderServiceClient);
  }

  @Test
  @DisplayName("배송 정보 생성")
  public void createDeliveryService() {
    // given
    List<DeliveryInsertRequestDto> dtos = createInsertRequestDto();

    // when
    List<Long> savedDeliveryIds = deliveryService.createDelivery(dtos);

    // then
    Assertions.assertEquals(savedDeliveryIds.size(), dtos.size());
    Assertions.assertNotNull(savedDeliveryIds.get(0));
  }

  @Test
  @DisplayName("배송 정보 조회")
  void getAllDeliveryInfo() {
    // given
    Delivery delivery1 = createDeliveryEntity("홍길동", "010-1111-1111");
    Delivery delivery2 = createDeliveryEntity("이순신", "010-2222-2222");
    deliveryRepository.saveAll(List.of(delivery1, delivery2));

    List<Delivery> foundDeliveries =
        deliveryRepository.findAllById(
            List.of(delivery1.getDeliveryId(), delivery2.getDeliveryId()));
    List<Long> deliveryIds =
        foundDeliveries.stream().map(Delivery::getDeliveryId).collect(Collectors.toList());

    // when
    List<DeliveryReadResponseDto> dtos = deliveryService.getDelivery(deliveryIds);

    // then
    assertThat(dtos).hasSize(2).extracting("ordererName").containsExactlyInAnyOrder("홍길동", "이순신");
  }

  @Test
  @DisplayName("배송 정보 수정")
  void updateDelivery() {
    // given
    Delivery delivery = createDeliveryEntity("홍길동", "010-1111-1111");

    DeliveryUpdateRequestDto dto =
        DeliveryUpdateRequestDto.builder()
            .recipientName("손흥민")
            .recipientPhoneNumber("010-5555-5555")
            .zipcode("04342")
            .roadName("서울시 용산구 한남동 독서당로 111-2")
            .addressDetail("1701호")
            .build();

    Delivery savedDelivery = deliveryRepository.save(delivery);

    // when
    Delivery updatedDelivery = deliveryService.updateDelivery(savedDelivery.getDeliveryId(), dto);

    // then
    Assertions.assertNotNull(updatedDelivery.getDeliveryId());
    Assertions.assertEquals(updatedDelivery.getDeliveryRecipientName(), "손흥민");
    Assertions.assertEquals(updatedDelivery.getDeliveryZipcode(), "04342");
    Assertions.assertEquals(updatedDelivery.getDeliveryRoadName(), "서울시 용산구 한남동 독서당로 111-2");
    Assertions.assertEquals(updatedDelivery.getDeliveryAddressDetail(), "1701호");
  }

  @Test
  @DisplayName("배송 상태 변경")
  void modifyDeliveryStatus() {
    // given
    Delivery delivery = createDeliveryEntity("홍길동", "010-1111-1111");

    Long deliveryOrderId = 1L;
    String status = "PROCESSING";

    Long savedDeliveryId = deliveryRepository.save(delivery).getDeliveryId();
    delivery.modifyStatus(status);

    SuccessResponse<Long> mockResponse = new SuccessResponse<>("200", "Success", savedDeliveryId);
    Mockito.when(orderServiceClient.getDeliveryId(deliveryOrderId)).thenReturn(mockResponse);

    // when
    Delivery modifiedStatusDelivery = deliveryService.changeStatus(deliveryOrderId, status);

    // then
    Assertions.assertEquals(modifiedStatusDelivery.getDeliveryStatus(), DeliveryStatus.PROCESSING);
  }

  @Test
  @DisplayName("잘못된 값으로 배송상태 변경 막기")
  void modifyWithWrongDeliveryStatus() {
    // given
    Delivery delivery = createDeliveryEntity("홍길동", "010-1111-1111");
    Long savedDeliveryId = deliveryRepository.save(delivery).getDeliveryId();

    Long orderId = 1L;
    String status = "PROCESSED";

    SuccessResponse<Long> mockResponse = new SuccessResponse<>("200", "Success", savedDeliveryId);
    Mockito.when(orderServiceClient.getDeliveryId(orderId)).thenReturn(mockResponse);

    // when, then
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> deliveryService.changeStatus(orderId, status));
  }

  @Test
  @DisplayName("이전 단계의 배송 상태로 변경은 불가능하다")
  void modifyWithPreviousDeliveryStatus() {
    // given
    Delivery delivery = createDeliveryEntity("홍길동", "010-1111-1111");
    Long savedDeliveryId = deliveryRepository.save(delivery).getDeliveryId();

    Long orderId = 1L;
    String status = "INITIAL";

    SuccessResponse<Long> mockResponse = new SuccessResponse<>("200", "Success", savedDeliveryId);
    Mockito.when(orderServiceClient.getDeliveryId(orderId)).thenReturn(mockResponse);

    // when, then
    Assertions.assertThrows(
        IllegalStateException.class, () -> deliveryService.changeStatus(orderId, status));
  }

  private List<DeliveryInsertRequestDto> createInsertRequestDto() {
    List<DeliveryInsertRequestDto> list = new ArrayList<>();

    list.add(
        DeliveryInsertRequestDto.builder()
            .ordererName("홍길동")
            .ordererPhoneNumber("010-1111-1111")
            .ordererEmail("abc@example.com")
            .recipientName("이순신")
            .recipientPhoneNumber("010-2222-2222")
            .zipcode("05231")
            .roadName("서울시 송파구 올림픽로 23가길 22-1")
            .addressDetail("401호")
            .request("빠른 배송 부탁드려요~")
            .deliveryCost(5000L)
            .build());

    return list;
  }

  private Delivery createDeliveryEntity(
      String ordererName, String ordererPhoneNumber) {
    return Delivery.builder()
        .deliveryOrdererName(ordererName)
        .deliveryOrdererPhoneNumber(ordererPhoneNumber)
        .deliveryOrdererEmail("abc@example.com")
        .deliveryRecipientName("이순신")
        .deliveryRecipientPhoneNumber("010-2222-2222")
        .deliveryZipcode("05231")
        .deliveryStatus(DeliveryStatus.PENDING)
        .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
        .deliveryAddressDetail("401호")
        .deliveryRequest("빠른 배송 부탁드려요~")
        .deliveryCost(5000L)
        .build();
  }
}
