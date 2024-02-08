package com.timzoeller.training.camunda8beispiel.payment;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Service;


@Service
public class PaymentWorkers {

    private Double credit = 1000.0;

    @JobWorker(type = "chargeCredit", autoComplete = false)
    public void paymentWorker(JobClient jobClient, ActivatedJob job) {

        Double totalAmount = (Double) job.getVariable("totalAmount");

        if(totalAmount > credit) {

            jobClient
                    .newThrowErrorCommand(job)
                    .errorCode("notEnoughCredit")
                    .errorMessage("You need to purchase more credits")
                    .send();
        } else {
            jobClient.newCompleteCommand(job).send();
        }

    }


}
