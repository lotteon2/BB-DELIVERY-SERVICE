package kr.bb.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DeliveryServiceTest {
    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("배송 정보 생성 service 테스트")
    public void createDeliveryService(){
        // given
        DeliveryInsertRequestDto dto = createInsertRequestDto();

        //when
        Delivery savedDelivery = deliveryService.createDelivery(dto);

        // then
        Assertions.assertNotNull(savedDelivery.getDeliveryId());
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
    }

    @Test
    @DisplayName("배송 정보 조회")
    void getAllDeliveryInfo() {
        // given
        Delivery delivery1 = createDeliveryEntity("홍길동", "010-1111-1111");
        Delivery delivery2 = createDeliveryEntity("이순신", "010-2222-2222");
        deliveryRepository.saveAll(List.of(delivery1, delivery2));

        List<Delivery> foundDeliveries = deliveryRepository.findAllById(List.of(delivery1.getDeliveryId(), delivery2.getDeliveryId()));
        List<Long> deliveryIds = foundDeliveries.stream().map(Delivery::getDeliveryId).collect(
                Collectors.toList());

        // when
        List<DeliveryReadResponseDto> dtos = deliveryService.getDelivery(deliveryIds);

        // then
        assertThat(dtos).hasSize(2)
                .extracting("ordererName")
                .containsExactlyInAnyOrder("홍길동", "이순신");
    }

    @Test
    @DisplayName("배송 정보 수정")
    void updateDelivery(){
        // given
        Delivery delivery = createDeliveryEntity("홍길동", "010-1111-1111");

        DeliveryUpdateRequestDto dto = DeliveryUpdateRequestDto.builder()
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

    private DeliveryInsertRequestDto createInsertRequestDto(){
        return DeliveryInsertRequestDto.builder()
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
    }

    private Delivery createDeliveryEntity(String ordererName, String ordererPhoneNumber){
        return Delivery.builder()
                .deliveryOrdererName(ordererName)
                .deliveryOrdererPhoneNumber(ordererPhoneNumber)
                .deliveryOrdererEmail("abc@example.com")
                .deliveryRecipientName("이순신")
                .deliveryRecipientPhoneNumber("010-2222-2222")
                .deliveryZipcode("05231")
                .deliveryRoadName("서울시 송파구 올림픽로 23가길 22-1")
                .deliveryAddressDetail("401호")
                .deliveryRequest("빠른 배송 부탁드려요~")
                .deliveryCost(5000L)
                .build();
    }

}
