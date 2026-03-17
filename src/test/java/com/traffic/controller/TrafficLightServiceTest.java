
package com.traffic.controller;

import com.traffic.controller.dto.HistoryResponseDTO;
import com.traffic.controller.dto.TrafficStateResponseDTO;
import com.traffic.controller.mapper.TrafficMapper;
import com.traffic.controller.model.LightTypes;
import com.traffic.controller.model.ListOfDirection;
import com.traffic.controller.service.TrafficLightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TrafficLightServiceTest {
    @Mock
    private TrafficMapper mapper;

    private TrafficLightService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TrafficLightService(mapper);
    }

    @Test
    void testInitialLightsState() {
        var lights = service.getLightDetails();
        assertEquals(LightTypes.RED, lights.get(ListOfDirection.NORTH).getState());
        assertEquals(LightTypes.RED, lights.get(ListOfDirection.SOUTH).getState());
        assertEquals(LightTypes.RED, lights.get(ListOfDirection.EAST).getState());
        assertEquals(LightTypes.RED, lights.get(ListOfDirection.WEST).getState());
    }

    @Test
    void testChangeLightSuccessfully() {
        service.changeLight(ListOfDirection.NORTH, LightTypes.GREEN);
        assertEquals(
                LightTypes.GREEN,
                service.getLightDetails().get(ListOfDirection.NORTH).getState()
        );
    }

    @Test
    void testPauseSystem() {
        service.pauseSystem();
        assertTrue(service.isPaused());
    }

    @Test
    void testResumeSystem() {
        service.pauseSystem();
        service.resumeTrafficLightSystem();
        assertFalse(service.isPaused());
    }

    @Test
    void testChangeLightWhenPausedThrowsException() {
        service.pauseSystem();
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        service.changeLight(ListOfDirection.NORTH, LightTypes.GREEN)
                );
        assertEquals("System is paused now", exception.getMessage());
    }

    @Test
    void testConflictValidationNorthSouth() {
        service.changeLight(ListOfDirection.EAST, LightTypes.GREEN);
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        service.changeLight(ListOfDirection.NORTH, LightTypes.GREEN)
                );
        assertEquals("Conflict detected for EAST/WEST GREEN", exception.getMessage());
    }

    @Test
    void testConflictValidationEastWest() {
        service.changeLight(ListOfDirection.NORTH, LightTypes.GREEN);
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        service.changeLight(ListOfDirection.EAST, LightTypes.GREEN)
                );
        assertEquals("Conflict detected for NORTH/SOUTH GREEN", exception.getMessage());
    }

    @Test
    void testGetCurrentState() {
        when(mapper.toTrafficStateDTO(any()))
                .thenReturn(new TrafficStateResponseDTO());
        List<TrafficStateResponseDTO> result = service.getCurrentStateDirections();
        assertEquals(4, result.size());
        verify(mapper, times(4))
                .toTrafficStateDTO(any());
    }

    @Test
    void testGetHistory() {
        when(mapper.toHistoryDTO(any()))
                .thenReturn(new HistoryResponseDTO("test", System.currentTimeMillis()));
        List<HistoryResponseDTO> history = service.getHistoryList();
        assertFalse(history.isEmpty());
        verify(mapper, atLeastOnce())
                .toHistoryDTO(any());
    }
}

