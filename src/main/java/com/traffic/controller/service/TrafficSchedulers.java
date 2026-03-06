package com.traffic.controller.service;

import com.traffic.controller.model.ListOfDirection;
import com.traffic.controller.model.LightTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrafficSchedulers {

    private final TrafficLightService trafficLightService;
    private int phaseDuration = 0;

    @Scheduled(fixedRate = 10000)
    public void cycleTrafficLights() {

        if (trafficLightService.isPaused()) {
            return;
        }
        try {
            switch (phaseDuration) {
                case 0 -> {
                    trafficLightService.changeLight(ListOfDirection.NORTH, LightTypes.GREEN);
                    trafficLightService.changeLight(ListOfDirection.SOUTH, LightTypes.GREEN);
                    trafficLightService.changeLight(ListOfDirection.EAST, LightTypes.RED);
                    trafficLightService.changeLight(ListOfDirection.WEST, LightTypes.RED);
                }
                case 1 -> {
                    trafficLightService.changeLight(ListOfDirection.NORTH, LightTypes.YELLOW);
                    trafficLightService.changeLight(ListOfDirection.SOUTH, LightTypes.YELLOW);
                }
                case 2 -> {
                    trafficLightService.changeLight(ListOfDirection.NORTH, LightTypes.RED);
                    trafficLightService.changeLight(ListOfDirection.SOUTH, LightTypes.RED);
                    trafficLightService.changeLight(ListOfDirection.EAST, LightTypes.GREEN);
                    trafficLightService.changeLight(ListOfDirection.WEST, LightTypes.GREEN);
                }
                case 3 -> {
                    trafficLightService.changeLight(ListOfDirection.EAST, LightTypes.YELLOW);
                    trafficLightService.changeLight(ListOfDirection.WEST, LightTypes.YELLOW);
                }
            }
            phaseDuration = (phaseDuration + 1) % 4;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}