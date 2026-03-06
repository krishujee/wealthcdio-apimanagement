package com.traffic.controller;

import com.traffic.controller.service.TrafficLightService;
import com.traffic.controller.service.TrafficScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TrafficSchedulerTest {

    @MockBean
    private TrafficLightService service;

    @Autowired
    private TrafficScheduler scheduler;

    @Test
    void testSchedulerRuns() throws Exception {

        Thread.sleep(11000);
        verify(service, atLeastOnce())
                .changeLight(any(), any());
    }
}
