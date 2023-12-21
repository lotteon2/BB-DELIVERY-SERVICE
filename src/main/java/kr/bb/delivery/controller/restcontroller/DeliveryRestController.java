package kr.bb.delivery.controller.restcontroller;

import java.util.List;
import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.dto.response.DeliveryAddressReadResponseDto;
import kr.bb.delivery.service.DeliveryAddressService;
import kr.bb.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryRestController {
    private final DeliveryService deliveryService;
    private final DeliveryAddressService deliveryAddressService;

    @PatchMapping("/{deliveryId}")
    public ResponseEntity<Void> updateDeliveryInfo(@PathVariable Long deliveryId, @RequestBody DeliveryUpdateRequestDto requestDto){
        deliveryService.updateDelivery(deliveryId, requestDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderDeliveryId}")
    public ResponseEntity<Void> changeDeliveryStatus(@PathVariable Long orderDeliveryId, @RequestBody String status){
        deliveryService.changeStatus(orderDeliveryId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delivery-address")
    public ResponseEntity<List<DeliveryAddressReadResponseDto>> getDeliveryAddress(@RequestHeader Long userId){
        return ResponseEntity.ok().body(
                deliveryAddressService.getDeliveryAddress(userId));
    }


}
