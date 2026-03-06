package com.traffic.controller.controller;

import com.traffic.controller.dto.HistoryResponseDTO;
import com.traffic.controller.dto.TrafficLightChangeRequestDTO;
import com.traffic.controller.dto.TrafficStateResponseDTO;
import com.traffic.controller.service.TrafficLightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("/traffic-controls")
@RequiredArgsConstructor
public class TrafficController {
    private final TrafficLightService service;

    @GetMapping("/state")
    public List<TrafficStateResponseDTO> getState() {
        return service.getCurrentStateDirections();
    }

    @GetMapping("/history")
    public List<HistoryResponseDTO> getHistory() {
        return service.getHistoryList();
    }

    @PostMapping("/changeing")
    public void changeLight(@RequestBody TrafficLightChangeRequestDTO request) {
        service.changeLight(request.getDirection(), request.getState());
    }

    @PostMapping("/pauseLight")
    public void pause() {
        service.pauseSystem();
    }

    @PostMapping("/resumeLight")
    public void resume() {
        service.resumeTrafficLightSystem();
    }

    @GetMapping("/status")
    public boolean status() {
        return service.isPaused();
    }
}