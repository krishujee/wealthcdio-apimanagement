package com.traffic.controller.model;

public class TrafficLight {
    private Direction direction;
    private LightState state;
    private long lastUpdated;

    public TrafficLight(Direction direction, LightState state) {
        this.direction = direction;
        this.state = state;
        this.lastUpdated = System.currentTimeMillis();
    }

    public synchronized void changeState(LightState newState) {
        this.state = newState;
        this.lastUpdated = System.currentTimeMillis();
    }

    public Direction getDirection() {
        return direction;
    }

    public LightState getState() {
        return state;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}
