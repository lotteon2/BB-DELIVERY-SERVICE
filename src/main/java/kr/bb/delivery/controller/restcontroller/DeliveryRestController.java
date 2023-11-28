package kr.bb.delivery.controller.restcontroller;

import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.entity.Delivery;
import kr.bb.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryRestController {
    private final DeliveryService deliveryService;

    @PatchMapping("/delivery/{deliveryId}")
    public ResponseEntity<Void> updateDeliveryInfo(@PathVariable Long deliveryId, @RequestBody DeliveryUpdateRequestDto requestDto){
        deliveryService.updateDelivery(deliveryId, requestDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/delivery/{orderDeliveryId}")
    public ResponseEntity<Void> changeDeliveryStatus(@PathVariable Long orderDeliveryId, @RequestBody String status){
        deliveryService.changeStatus(orderDeliveryId, status);

        return ResponseEntity.ok().build();
    }
}
