package com.traffic.controller.dto;

import com.traffic.controller.model.Direction;
import com.traffic.controller.model.LightState;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TrafficLightChangeRequestDTO {

    private Direction direction;
    private LightState state;

}
