package com.traffic.controller.dto;

import com.traffic.controller.model.ListOfDirection;
import com.traffic.controller.model.LightTypes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TrafficLightChangeRequestDTO {
    private ListOfDirection direction;
    private LightTypes state;

}
