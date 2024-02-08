package com.timzoeller.training.camunda8beispiel.payment;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.BackoffSupplier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Configuration
public class PaymentProcessService {

    private final ZeebeClient zeebeClient;

    public PaymentProcessService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
        //startPaymentProcess(1.00);
        correlateMessage(20.00);
    }

    public void startPaymentProcess(Double totalAmount) {
       zeebeClient
               .newCreateInstanceCommand()
               .bpmnProcessId("Process_Payment")
               .latestVersion()
               .variables(Map.of("totalAmount", totalAmount, "orderId", UUID.randomUUID().toString()))
               .send();
    }

    public void correlateMessage(Double totalAmount) {
        zeebeClient.newPublishMessageCommand()
                .messageName("ReceiveMessage")
                .correlationKey("c1de5f32-7aab-477d-a4e4-1554f6862210")
                .variables(Map.of("totalAmount", totalAmount, "orderId", UUID.randomUUID().toString()))
                .send();
    }

}
