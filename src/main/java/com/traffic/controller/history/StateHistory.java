package com.traffic.controller.history;

import lombok.Getter;

@Getter
public class StateHistory {
        private String eventType;
        private long timestamp;

        public StateHistory(String eventType) {
            this.eventType = eventType;
            this.timestamp = System.currentTimeMillis();
        }

}
