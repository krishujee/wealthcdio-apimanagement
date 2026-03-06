package com.traffic.controller.dto;

import com.traffic.controller.model.Direction;
import com.traffic.controller.model.LightState;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficStateResponseDTO {
    private Direction direction;
    private LightState state;
    private long lastUpdated;


}
