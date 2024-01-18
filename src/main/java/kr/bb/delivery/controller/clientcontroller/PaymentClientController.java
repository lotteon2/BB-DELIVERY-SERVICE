package kr.bb.delivery.controller.clientcontroller;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/delivery/")
public class PaymentClientController {
  private final DeliveryService deliveryService;

  @PostMapping("subscription")
  public CommonResponse<List<Long>> createDeliveryForSubscription(
      @RequestBody List<Long> oldDeliveryIds) {
    return CommonResponse.success(deliveryService.createDeliveryForSubscription(oldDeliveryIds));
  }
}
