package com.traffic.controller.history;

import lombok.Getter;

@Getter
public class StateHistory {
        private String event;
        private long timestamp;

        public StateHistory(String event) {
            this.event = event;
            this.timestamp = System.currentTimeMillis();
        }

}
