package kr.bb.delivery.feign;

import bloomingblooms.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="orderServiceClient", url = "${endpoint.order-service}")
public interface OrderServiceClient {
    @GetMapping("/client/{orderId}/delivery-id")
    CommonResponse<Long> getDeliveryId(@PathVariable String orderId);
}
