package kr.bb.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

import bloomingblooms.domain.delivery.UpdateOrderStatusDto;
import bloomingblooms.domain.notification.delivery.DeliveryStatus;
import bloomingblooms.response.CommonResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.bb.delivery.client.OrderServiceClient;
import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.dto.response.DeliveryReadResponseDto;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.kafka.KafkaProducer;
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
  @MockBean private KafkaProducer<UpdateOrderStatusDto> orderStatusDtoKafkaProducer;

  @BeforeEach
  void setup() {
    deliveryService =
        new DeliveryService(deliveryRepository, orderServiceClient, orderStatusDtoKafkaProducer);
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

    String orderDeliveryId = "가게주문id";
    String status = "PROCESSING";

    Long savedDeliveryId = deliveryRepository.save(delivery).getDeliveryId();
    delivery.modifyStatus(DeliveryStatus.valueOf(status));

    Mockito.when(orderServiceClient.getDeliveryId(orderDeliveryId))
        .thenReturn(CommonResponse.success(savedDeliveryId));
    doNothing().when(orderStatusDtoKafkaProducer).send(eq("order-delivery-status"), any());

    // when
    Delivery modifiedStatusDelivery = deliveryService.changeStatus(orderDeliveryId, status);

    // then
    Assertions.assertEquals(modifiedStatusDelivery.getDeliveryStatus(), DeliveryStatus.PROCESSING);
  }

  @Test
  @DisplayName("잘못된 값으로 배송상태 변경 막기")
  void modifyWithWrongDeliveryStatus() {
    // given
    Delivery delivery = createDeliveryEntity("홍길동", "010-1111-1111");
    Long savedDeliveryId = deliveryRepository.save(delivery).getDeliveryId();

    String orderId = "가게주문id";
    String status = "PROCESSED";

    Mockito.when(orderServiceClient.getDeliveryId(orderId))
        .thenReturn(CommonResponse.success(savedDeliveryId));

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

    String orderId = "가게주문id";
    String status = "PENDING";

    Mockito.when(orderServiceClient.getDeliveryId(orderId))
        .thenReturn(CommonResponse.success(savedDeliveryId));

    // when, then
    Assertions.assertThrows(
        IllegalStateException.class, () -> deliveryService.changeStatus(orderId, status));
  }

  @Test
  @DisplayName("정기 구독 주문 발생시 신규 배송을 마지막 배송과 동일하게 생성한다")
  void createDeliveryForSubscription(){
    // given
    List<Long> oldDeliveryIds = List.of(1L);
    // when
    List<Long> newDeliveryIds = deliveryService.createDeliveryForSubscription(
            oldDeliveryIds);
    // then
    assertThat(newDeliveryIds.size()).isEqualTo(1L);
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

  private Delivery createDeliveryEntity(String ordererName, String ordererPhoneNumber) {
    return Delivery.builder()
        .deliveryOrdererName(ordererName)
        .deliveryOrdererPhoneNumber(ordererPhoneNumber)
        .deliveryOrdererEmail("abc@example.com")
        .deliveryRecipientName("이순신")
        .deliveryRecipientPhoneNumber("010-2222-2222")
        .deliveryZipcode("05231")
        .deliveryStatus(DeliveryStatus.PROCESSING)
        .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
        .deliveryAddressDetail("401호")
        .deliveryRequest("빠른 배송 부탁드려요~")
        .deliveryCost(5000L)
        .build();
  }
}
