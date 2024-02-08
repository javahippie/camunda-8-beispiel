package com.timzoeller.training.camunda8beispiel;

import io.camunda.zeebe.client.ZeebeClient;

public class TestApp {

    public static void main(String... args) {
        var client = ZeebeClient
                .newClientBuilder()
                .gatewayAddress("localhost:26500")
                .usePlaintext()
                .build();

        client.newWorker().jobType("chargeCredit").handler((client1, job) ->
        {
            System.out.println("Ein Job, hurrah!");
        }).open();
    }
}
