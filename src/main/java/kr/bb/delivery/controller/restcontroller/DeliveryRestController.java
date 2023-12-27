package kr.bb.delivery.controller.restcontroller;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.dto.response.DeliveryAddressReadResponseDto;
import kr.bb.delivery.service.DeliveryAddressService;
import kr.bb.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryRestController {
  private final DeliveryService deliveryService;
  private final DeliveryAddressService deliveryAddressService;

  @PatchMapping("/{deliveryId}")
  public void updateDeliveryInfo(
      @PathVariable Long deliveryId, @RequestBody DeliveryUpdateRequestDto requestDto) {
    deliveryService.updateDelivery(deliveryId, requestDto);
  }

  @PatchMapping("/{orderDeliveryId}/{status}")
  public void changeDeliveryStatus(
      @PathVariable String orderDeliveryId, @PathVariable String status) {
    deliveryService.changeStatus(orderDeliveryId, status);
  }

  @GetMapping("/delivery-address")
  public CommonResponse<List<DeliveryAddressReadResponseDto>> getDeliveryAddress(
      @RequestHeader Long userId) {
    return CommonResponse.success(deliveryAddressService.getDeliveryAddress(userId));
  }
}
