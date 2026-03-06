package com.traffic.controller.controller;

import com.traffic.controller.dto.HistoryResponseDTO;
import com.traffic.controller.dto.TrafficLightChangeRequestDTO;
import com.traffic.controller.dto.TrafficStateResponseDTO;
import com.traffic.controller.service.TrafficLightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traffic")
@RequiredArgsConstructor
public class TrafficController {
    private final TrafficLightService service;

    @GetMapping("/state")
    public List<TrafficStateResponseDTO> getState() {
        return service.getCurrentState();
    }

    @GetMapping("/history")
    public List<HistoryResponseDTO> getHistory() {
        return service.getHistory();
    }

    @PostMapping("/change")
    public void changeLight(@RequestBody TrafficLightChangeRequestDTO request) {
        service.changeLight(request.getDirection(), request.getState());
    }

    @PostMapping("/pause")
    public void pause() {
        service.pauseSystem();
    }

    @PostMapping("/resume")
    public void resume() {
        service.resumeSystem();
    }

    @GetMapping("/status")
    public boolean status() {
        return service.isPaused();
    }
}