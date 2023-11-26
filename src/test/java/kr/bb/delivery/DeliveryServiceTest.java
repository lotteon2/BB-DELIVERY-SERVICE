package kr.bb.delivery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {
    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("배송 정보 생성 service 테스트")
    public void createDeliveryService(){
        // given
        DeliveryInsertRequestDto dto = DeliveryInsertRequestDto.builder()
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

        Delivery mockDelivery = Delivery.builder()
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

        Mockito.when(deliveryRepository.save(any(Delivery.class))).thenReturn(mockDelivery);
//        given(deliveryRepository.save(any())).willReturn(mockDelivery);

        //when
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

}
