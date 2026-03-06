package com.traffic.controller.model;

public class TrafficLight {
    private ListOfDirection direction;
    private LightTypes state;
    private long lastUpdated;

    public TrafficLight(ListOfDirection direction, LightTypes state) {
        this.direction = direction;
        this.state = state;
        this.lastUpdated = System.currentTimeMillis();
    }

    public synchronized void changeState(LightTypes newState) {
        this.state = newState;
        this.lastUpdated = System.currentTimeMillis();
    }

    public ListOfDirection getDirection() {
        return direction;
    }

    public LightTypes getState() {
        return state;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}
