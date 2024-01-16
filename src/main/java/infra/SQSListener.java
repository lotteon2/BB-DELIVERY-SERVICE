package infra;

import java.time.LocalDateTime;
import java.util.Map;
import kr.bb.delivery.service.DeliverySqsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SQSListener {
    private final DeliverySqsService deliverySqsService;

    @SqsListener(
            value="${cloud.aws.sqs.subscription-delivery-update-queue.name}",
            deletionPolicy= SqsMessageDeletionPolicy.NEVER)
    public void consumeSubscriptionDeliveryUpdateQueue(
            @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack){
        LocalDateTime now = LocalDateTime.now();
        deliverySqsService.updateSubscriptionDelivery(now);
        ack.acknowledge();
    }
}
