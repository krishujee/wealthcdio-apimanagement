package com.traffic.controller.service;

import com.traffic.controller.dto.HistoryResponseDTO;
import com.traffic.controller.dto.TrafficStateResponseDTO;
import com.traffic.controller.history.StateHistory;
import com.traffic.controller.mapper.TrafficMapper;
import com.traffic.controller.model.Direction;
import com.traffic.controller.model.LightState;
import com.traffic.controller.model.TrafficLight;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TrafficLightService {
    private final Map<Direction, TrafficLight> lights = new ConcurrentHashMap<>();
    private final Queue<StateHistory> history = new ConcurrentLinkedQueue<>();
    private final Lock lock = new ReentrantLock();
    private volatile boolean paused = false;
    private final TrafficMapper mapper;

    public TrafficLightService(TrafficMapper mapper) {
        this.mapper = mapper;
        lights.put(Direction.NORTH, new TrafficLight(Direction.NORTH, LightState.RED));
        lights.put(Direction.SOUTH, new TrafficLight(Direction.SOUTH, LightState.RED));
        lights.put(Direction.EAST, new TrafficLight(Direction.EAST, LightState.RED));
        lights.put(Direction.WEST, new TrafficLight(Direction.WEST, LightState.RED));
        history.add(new StateHistory("Traffic system initialized"));
    }

    public void changeLight(Direction direction, LightState newState) {
        lock.lock();
        try {
            if (paused) {
                throw new IllegalStateException("System is paused");
            }
            if (newState == LightState.GREEN) {
                validateConflict(direction);
            }
            TrafficLight light = lights.get(direction);
            light.changeState(newState);
            history.add(new StateHistory(direction + " -> " + newState));
        } finally {
            lock.unlock();
        }
    }

    private void validateConflict(Direction direction) {

        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            if (lights.get(Direction.EAST).getState() == LightState.GREEN ||
                    lights.get(Direction.WEST).getState() == LightState.GREEN) {
                throw new IllegalStateException("Conflict detected EAST/WEST GREEN");
            }
        }

        if (direction == Direction.EAST || direction == Direction.WEST) {
            if (lights.get(Direction.NORTH).getState() == LightState.GREEN ||
                    lights.get(Direction.SOUTH).getState() == LightState.GREEN) {
                throw new IllegalStateException("Conflict detected NORTH/SOUTH GREEN");
            }
        }
    }

    public List<TrafficStateResponseDTO> getCurrentState() {
        return lights.values().stream().map(mapper::toTrafficStateDTO).toList();
    }

    public List<HistoryResponseDTO> getHistory() {
        return history.stream().map(mapper::toHistoryDTO).toList();
    }

    public void pauseSystem() {
        paused = true;
        history.add(new StateHistory("System paused"));
    }

    public void resumeSystem() {
        paused = false;
        history.add(new StateHistory("System resumed"));
    }

    public boolean isPaused() {
        return paused;
    }

    public Map<Direction, TrafficLight> getLights() {
        return lights;
    }

}