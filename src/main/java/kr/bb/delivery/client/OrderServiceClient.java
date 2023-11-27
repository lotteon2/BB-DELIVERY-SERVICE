package kr.bb.delivery.client;

import bloomingblooms.response.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="orderServiceClient", url = "${endpoint.order-service")
public interface OrderServiceClient {
    @GetMapping("/orders/{orderId}/delivery-id")
    SuccessResponse<Long> getDeliveryId(@PathVariable Long orderId);
}
