package kr.bb.delivery.controller.clientcontroller;

import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class OrderClientController {
    private final DeliveryService deliveryService;
    @PostMapping("")
    public ResponseEntity<Void> createDelivery(@RequestBody DeliveryInsertRequestDto requestDto){
        deliveryService.createDelivery(requestDto);
        return ResponseEntity.ok().build();
    }
}
