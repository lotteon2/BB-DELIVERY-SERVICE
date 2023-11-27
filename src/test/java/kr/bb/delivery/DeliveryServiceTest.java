package kr.bb.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import bloomingblooms.response.SuccessResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kr.bb.delivery.client.OrderServiceClient;
import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.dto.response.DeliveryReadResponseDto;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.entity.DeliveryStatus;
import kr.bb.delivery.repository.DeliveryRepository;
import kr.bb.delivery.service.DeliveryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {
  @InjectMocks private DeliveryService deliveryService;
  @Mock private DeliveryRepository deliveryRepository;
  @Mock private OrderServiceClient orderServiceClient;

  @Test
  @DisplayName("배송 정보 생성 service 테스트")
  public void createDeliveryService() {
    // given
    DeliveryInsertRequestDto dto =
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
            .build();

    Delivery mockDelivery =
        Delivery.builder()
            .deliveryId(1L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("이순신")
            .deliveryRecipientPhoneNumber("010-2222-2222")
            .deliveryZipcode("05231")
            .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
            .deliveryAddressDetail("401호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryCost(5000L)
            .build();

    //        Mockito.when(deliveryRepository.save(any(Delivery.class))).thenReturn(mockDelivery);
    given(deliveryRepository.save(any())).willReturn(mockDelivery);

    // when
    Delivery savedDelivery = deliveryService.createDelivery(dto);

    // then
    Assertions.assertEquals(savedDelivery.getDeliveryId(), 1);
    Assertions.assertNull(savedDelivery.getDeliveryTrackingNumber());
    Assertions.assertEquals(savedDelivery.getDeliveryOrdererName(), "홍길동");
    Assertions.assertEquals(savedDelivery.getDeliveryOrdererPhoneNumber(), "010-1111-1111");
    Assertions.assertEquals(savedDelivery.getDeliveryOrdererEmail(), "abc@example.com");
    Assertions.assertEquals(savedDelivery.getDeliveryRecipientName(), "이순신");
    Assertions.assertEquals(savedDelivery.getDeliveryRecipientPhoneNumber(), "010-2222-2222");
    Assertions.assertEquals(savedDelivery.getDeliveryZipcode(), "05231");
    Assertions.assertEquals(savedDelivery.getDeliveryRoadName(), "서울시 송파구 올림픽로 23가길 22-1");
    Assertions.assertEquals(savedDelivery.getDeliveryAddressDetail(), "401호");
    Assertions.assertEquals(savedDelivery.getDeliveryRequest(), "빠른 배송 부탁드려요~");
    Assertions.assertEquals(savedDelivery.getDeliveryCost(), 5000L);
    Assertions.assertEquals(savedDelivery.getDeliveryStatus(), DeliveryStatus.PENDING);

    verify(deliveryRepository).save(any());
  }

  @Test
  @DisplayName("배송 정보 조회")
  void getAllDeliveryInfo() {
    // given
    Delivery delivery1 =
        Delivery.builder()
            .deliveryId(1L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("이순신")
            .deliveryRecipientPhoneNumber("010-2222-2222")
            .deliveryZipcode("05231")
            .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
            .deliveryAddressDetail("401호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryCost(5000L)
            .build();

    Delivery delivery2 =
        Delivery.builder()
            .deliveryId(2L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("이순신")
            .deliveryRecipientPhoneNumber("010-2222-2222")
            .deliveryZipcode("05231")
            .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
            .deliveryAddressDetail("401호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryCost(5000L)
            .build();

    List<Long> deliveryIds = Arrays.asList(1L, 2L);

    //
    // Mockito.when(deliveryRepository.findAllById(deliveryIds)).thenReturn(List.of(delivery1,
    // delivery2));
    given(deliveryRepository.findAllById(deliveryIds)).willReturn(List.of(delivery1, delivery2));

    // when
    List<DeliveryReadResponseDto> dtos = deliveryService.getDelivery(deliveryIds);

    // then
    assertThat(dtos)
        .hasSize(2)
        .extracting("deliveryId", "ordererName")
        .containsExactlyInAnyOrder(tuple(1L, "홍길동"), tuple(2L, "홍길동"));
  }

  @Test
  @DisplayName("배송 정보 수정")
  void updateDelivery() {
    // given
    Delivery savedDelivery =
        Delivery.builder()
            .deliveryId(1L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("이순신")
            .deliveryRecipientPhoneNumber("010-2222-2222")
            .deliveryZipcode("05231")
            .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
            .deliveryAddressDetail("401호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryCost(5000L)
            .build();

    DeliveryUpdateRequestDto dto =
        DeliveryUpdateRequestDto.builder()
            .recipientName("손흥민")
            .recipientPhoneNumber("010-5555-5555")
            .zipcode("04342")
            .roadName("서울시 용산구 한남동 독서당로 111-2")
            .addressDetail("1701호")
            .build();

    Delivery modifiedDelivery =
        Delivery.builder()
            .deliveryId(1L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("손흥민")
            .deliveryRecipientPhoneNumber("010-5555-5555")
            .deliveryZipcode("04342")
            .deliveryRoadName("서울시 용산구 한남동 독서당로 111-2")
            .deliveryAddressDetail("1701호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryCost(5000L)
            .build();

    Long deliveryId = 1L;

    given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(savedDelivery));
    given(deliveryRepository.save(any())).willReturn(modifiedDelivery);

    // when
    Delivery updatedDelivery = deliveryService.updateDelivery(deliveryId, dto);

    // then
    Assertions.assertEquals(updatedDelivery.getDeliveryId(), 1L);
    Assertions.assertEquals(updatedDelivery.getDeliveryRecipientName(), "손흥민");
    Assertions.assertEquals(updatedDelivery.getDeliveryZipcode(), "04342");
    Assertions.assertEquals(updatedDelivery.getDeliveryRoadName(), "서울시 용산구 한남동 독서당로 111-2");
    Assertions.assertEquals(updatedDelivery.getDeliveryAddressDetail(), "1701호");
  }

  @Test
  @DisplayName("배송 상태 변경")
  void modifyDeliveryStatus() {
    // given
    Delivery savedDelivery =
        Delivery.builder()
            .deliveryId(1L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("이순신")
            .deliveryRecipientPhoneNumber("010-2222-2222")
            .deliveryZipcode("05231")
            .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
            .deliveryAddressDetail("401호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryStatus(DeliveryStatus.PENDING)
            .deliveryCost(5000L)
            .build();

    Delivery modifiedDelivery =
        Delivery.builder()
            .deliveryId(1L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("손흥민")
            .deliveryRecipientPhoneNumber("010-5555-5555")
            .deliveryZipcode("04342")
            .deliveryRoadName("서울시 용산구 한남동 독서당로 111-2")
            .deliveryAddressDetail("1701호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryCost(5000L)
            .deliveryStatus(DeliveryStatus.PROCESSING)
            .build();

    SuccessResponse<Long> mockResponse = new SuccessResponse<>("200", "Success", 1L);

    Long deliveryOrderId = 1L;
    String status = "PROCESSING";

    given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(savedDelivery));
    given(deliveryRepository.save(any())).willReturn(modifiedDelivery);
    given(orderServiceClient.getDeliveryId(deliveryOrderId)).willReturn(mockResponse);

    // when
    Delivery modifiedStatusDelivery = deliveryService.changeStatus(deliveryOrderId, status);

    // then
    Assertions.assertEquals(modifiedStatusDelivery.getDeliveryStatus(), DeliveryStatus.PROCESSING);
  }

  @Test
  @DisplayName("잘못된 값으로 배송상태 변경은 불가능하다")
  void modifyWithWrongDeliveryStatus() {
    // given
    Delivery savedDelivery =
        Delivery.builder()
            .deliveryId(1L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("이순신")
            .deliveryRecipientPhoneNumber("010-2222-2222")
            .deliveryZipcode("05231")
            .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
            .deliveryAddressDetail("401호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryStatus(DeliveryStatus.PENDING)
            .deliveryCost(5000L)
            .build();

    SuccessResponse<Long> mockResponse = new SuccessResponse<>("200", "Success", 1L);

    Long deliveryOrderId = 1L;
    String status = "PROCESSED";

    given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(savedDelivery));
    given(orderServiceClient.getDeliveryId(deliveryOrderId)).willReturn(mockResponse);

    // when, then
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> deliveryService.changeStatus(deliveryOrderId, status));
  }

  @Test
  @DisplayName("이전 단계의 배송 상태로 변경은 불가능하다")
  void modifyWithPreviousDeliveryStatus() {
    // given
    Delivery savedDelivery =
        Delivery.builder()
            .deliveryId(1L)
            .deliveryOrdererName("홍길동")
            .deliveryOrdererPhoneNumber("010-1111-1111")
            .deliveryOrdererEmail("abc@example.com")
            .deliveryRecipientName("이순신")
            .deliveryRecipientPhoneNumber("010-2222-2222")
            .deliveryZipcode("05231")
            .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
            .deliveryAddressDetail("401호")
            .deliveryRequest("빠른 배송 부탁드려요~")
            .deliveryStatus(DeliveryStatus.PROCESSING)
            .deliveryCost(5000L)
            .build();

    SuccessResponse<Long> mockResponse = new SuccessResponse<>("200", "Success", 1L);

    Long deliveryOrderId = 1L;
    String status = "PENDING";

    given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(savedDelivery));
    given(orderServiceClient.getDeliveryId(deliveryOrderId)).willReturn(mockResponse);

    // when, then
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> deliveryService.changeStatus(deliveryOrderId, status));
  }
}
