package com.timzoeller.training.camunda8beispiel;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.process.test.assertions.DeploymentAssert;
import io.camunda.zeebe.process.test.assertions.ProcessInstanceAssert;
import io.camunda.zeebe.process.test.extension.ZeebeProcessTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@ZeebeProcessTest
class Camunda8BeispielApplicationTests {

    private ZeebeTestEngine engine;
    private ZeebeClient client;

    @BeforeEach
    void deployProcess() {
        client.newDeployCommand()
                .addResourceFromClasspath("diagram_1.bpmn")
                .send()
                .join();
    }

    @Test
    void testStartProcessInstance() {
        ProcessInstanceEvent event = client.newCreateInstanceCommand()
                .bpmnProcessId("Process_Payment")
                .latestVersion()
                .send()
                .join();

        BpmnAssert
                .assertThat(event)
                .isActive()
                .isWaitingAtElements("Activity_0aa8jey");
    }

    @Test
    void testGateway() {
        ProcessInstanceEvent event = client.newCreateInstanceCommand()
                .bpmnProcessId("Process_Payment")
                .latestVersion()
                .startBeforeElement("Gateway_0vcnul5")
                .variable("remainingCharge",0)
                .send()
                .join();

        BpmnAssert
                .assertThat(event)
                .isCompleted()
                .hasPassedElement("Event_0py26x9");
    }

}
