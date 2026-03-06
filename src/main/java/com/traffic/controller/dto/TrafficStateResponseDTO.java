package com.traffic.controller.dto;

import com.traffic.controller.model.ListOfDirection;
import com.traffic.controller.model.LightTypes;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficStateResponseDTO {
    private ListOfDirection direction;
    private LightTypes state;
    private long lastUpdated;


}
