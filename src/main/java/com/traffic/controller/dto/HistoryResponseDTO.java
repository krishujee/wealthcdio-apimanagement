package com.traffic.controller.dto;

import lombok.Getter;

@Getter
public class HistoryResponseDTO {
    private String eventType;
    private long timestamp;

    public HistoryResponseDTO(String event, long timestamp) {
        this.eventType = event;
        this.timestamp = timestamp;
    }
}
