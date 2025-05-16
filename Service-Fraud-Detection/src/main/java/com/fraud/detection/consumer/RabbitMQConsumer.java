package com.fraud.detection.consumer;

import com.fraud.detection.dto.TransactionCheckFraudResponse;
import com.fraud.detection.service.FraudDetectionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQConsumer {

    @Autowired
    private FraudDetectionServiceImpl fraudDetectionService;

    @RabbitListener(queues = "${rabbitmq.queue.listener}", concurrency = "5-10")
    public void checkFraud(TransactionCheckFraudResponse response) {
        Thread.startVirtualThread(() -> {
            log.info("RabbitMQConsumer consume: " + response.getTransactionalCode());
            fraudDetectionService.detectionFraudRules(response);
        });
    }
}
