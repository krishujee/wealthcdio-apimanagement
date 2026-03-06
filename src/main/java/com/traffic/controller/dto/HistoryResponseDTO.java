package com.traffic.controller.dto;

import lombok.Getter;

@Getter
public class HistoryResponseDTO {
    private String event;
    private long timestamp;

    public HistoryResponseDTO(String event, long timestamp) {
        this.event = event;
        this.timestamp = timestamp;
    }
}
