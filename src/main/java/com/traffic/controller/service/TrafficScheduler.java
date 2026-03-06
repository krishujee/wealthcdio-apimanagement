package com.traffic.controller.service;

import com.traffic.controller.model.Direction;
import com.traffic.controller.model.LightState;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TrafficScheduler {

    private final TrafficLightService service;
    private int phase = 0;

    @Scheduled(fixedRate = 10000)
    public void cycleLights() {

        if (service.isPaused()) {
            return;
        }
        try {
            switch (phase) {
                case 0 -> {
                    service.changeLight(Direction.NORTH, LightState.GREEN);
                    service.changeLight(Direction.SOUTH, LightState.GREEN);
                    service.changeLight(Direction.EAST, LightState.RED);
                    service.changeLight(Direction.WEST, LightState.RED);
                }
                case 1 -> {
                    service.changeLight(Direction.NORTH, LightState.YELLOW);
                    service.changeLight(Direction.SOUTH, LightState.YELLOW);
                }
                case 2 -> {
                    service.changeLight(Direction.NORTH, LightState.RED);
                    service.changeLight(Direction.SOUTH, LightState.RED);
                    service.changeLight(Direction.EAST, LightState.GREEN);
                    service.changeLight(Direction.WEST, LightState.GREEN);
                }
                case 3 -> {
                    service.changeLight(Direction.EAST, LightState.YELLOW);
                    service.changeLight(Direction.WEST, LightState.YELLOW);
                }
            }
            phase = (phase + 1) % 4;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}