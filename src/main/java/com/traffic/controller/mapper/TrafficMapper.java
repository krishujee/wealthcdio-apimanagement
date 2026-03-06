package com.traffic.controller.mapper;


import com.traffic.controller.dto.HistoryResponseDTO;
import com.traffic.controller.dto.TrafficStateResponseDTO;
import com.traffic.controller.history.StateHistory;
import com.traffic.controller.model.TrafficLight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrafficMapper {
    TrafficStateResponseDTO toTrafficStateDTO(TrafficLight trafficLight);
    HistoryResponseDTO toHistoryDTO(StateHistory history);
}