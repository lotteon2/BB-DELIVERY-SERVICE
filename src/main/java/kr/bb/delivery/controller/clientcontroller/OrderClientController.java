package kr.bb.delivery.controller.clientcontroller;

import bloomingblooms.domain.delivery.DeliveryAddressInsertDto;
import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.delivery.dto.request.DeliveryInsertRequestDto;
import kr.bb.delivery.dto.response.DeliveryReadResponseDto;
import kr.bb.delivery.service.DeliveryAddressService;
import kr.bb.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class OrderClientController {
  private final DeliveryService deliveryService;
  private final DeliveryAddressService deliveryAddressService;

  @PostMapping("")
  public CommonResponse<List<Long>> createDelivery(@RequestBody List<DeliveryInsertRequestDto> requestDto) {
    List<Long> deliveryIds = deliveryService.createDelivery(requestDto);
    return CommonResponse.success(deliveryIds);
  }

  @GetMapping("")
  public CommonResponse<List<DeliveryReadResponseDto>> getDelivery(@RequestBody List<Long> deliveryIds) {
    return CommonResponse.success(deliveryService.getDelivery(deliveryIds));
  }

  @PostMapping("/delivery-address")
  public void createDeliveryAddress(@RequestBody DeliveryAddressInsertDto requestDto){
    deliveryAddressService.createDeliveryAddress(requestDto);
  }
}
