package com.traffic.controller.service;

import com.traffic.controller.dto.HistoryResponseDTO;
import com.traffic.controller.dto.TrafficStateResponseDTO;
import com.traffic.controller.history.StateHistory;
import com.traffic.controller.mapper.TrafficMapper;
import com.traffic.controller.model.ListOfDirection;
import com.traffic.controller.model.LightTypes;
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
    private final Map<ListOfDirection, TrafficLight> lights = new ConcurrentHashMap<>();
    private final Queue<StateHistory> history = new ConcurrentLinkedQueue<>();
    private final Lock lock = new ReentrantLock();
    private volatile boolean paused = false;
    private final TrafficMapper mapper;

    public TrafficLightService(TrafficMapper mapper) {
        this.mapper = mapper;
        lights.put(ListOfDirection.NORTH, new TrafficLight(ListOfDirection.NORTH, LightTypes.RED));
        lights.put(ListOfDirection.SOUTH, new TrafficLight(ListOfDirection.SOUTH, LightTypes.RED));
        lights.put(ListOfDirection.EAST, new TrafficLight(ListOfDirection.EAST, LightTypes.RED));
        lights.put(ListOfDirection.WEST, new TrafficLight(ListOfDirection.WEST, LightTypes.RED));
        history.add(new StateHistory("Traffic system initialized"));
    }

    public void changeLight(ListOfDirection direction, LightTypes newState) {
        lock.lock();
        try {
            if (paused) {
                throw new IllegalStateException("System is paused now");
            }
            if (newState == LightTypes.GREEN) {
                validateConflict(direction);
            }
            TrafficLight light = lights.get(direction);
            light.changeState(newState);
            history.add(new StateHistory(direction + " -> " + newState));
        } finally {
            lock.unlock();
        }
    }

    private void validateConflict(ListOfDirection direction) {
        if (direction == ListOfDirection.NORTH || direction == ListOfDirection.SOUTH) {
            if (lights.get(ListOfDirection.EAST).getState() == LightTypes.GREEN ||
                    lights.get(ListOfDirection.WEST).getState() == LightTypes.GREEN) {
                throw new IllegalStateException("Conflict detected for EAST/WEST GREEN");
            }
        }
        if (direction == ListOfDirection.EAST || direction == ListOfDirection.WEST) {
            if (lights.get(ListOfDirection.NORTH).getState() == LightTypes.GREEN ||
                    lights.get(ListOfDirection.SOUTH).getState() == LightTypes.GREEN) {
                throw new IllegalStateException("Conflict detected for NORTH/SOUTH GREEN");
            }
        }
    }

    public List<TrafficStateResponseDTO> getCurrentStateDirections() {
        return lights.values().stream().map(mapper::toTrafficStateDTO).toList();
    }

    public List<HistoryResponseDTO> getHistoryList() {
        return history.stream().map(mapper::toHistoryDTO).toList();
    }

    public void pauseSystem() {
        paused = true;
        history.add(new StateHistory("System paused"));
    }

    public void resumeTrafficLightSystem() {
        paused = false;
        history.add(new StateHistory("System resumed"));
    }

    public boolean isPaused() {
        return paused;
    }

    public Map<ListOfDirection, TrafficLight> getLightDetails() {
        return lights;
    }

}