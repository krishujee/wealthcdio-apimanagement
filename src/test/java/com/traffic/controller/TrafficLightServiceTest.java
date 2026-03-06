
package com.traffic.controller;

import com.traffic.controller.dto.HistoryResponseDTO;
import com.traffic.controller.dto.TrafficStateResponseDTO;
import com.traffic.controller.mapper.TrafficMapper;
import com.traffic.controller.model.Direction;
import com.traffic.controller.model.LightState;
import com.traffic.controller.service.TrafficLightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

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
        var lights = service.getLights();
        assertEquals(LightState.RED, lights.get(Direction.NORTH).getState());
        assertEquals(LightState.RED, lights.get(Direction.SOUTH).getState());
        assertEquals(LightState.RED, lights.get(Direction.EAST).getState());
        assertEquals(LightState.RED, lights.get(Direction.WEST).getState());
    }

    @Test
    void testChangeLightSuccessfully() {
        service.changeLight(Direction.NORTH, LightState.GREEN);
        assertEquals(
                LightState.GREEN,
                service.getLights().get(Direction.NORTH).getState()
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
        service.resumeSystem();
        assertFalse(service.isPaused());
    }

    @Test
    void testChangeLightWhenPausedThrowsException() {
        service.pauseSystem();
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        service.changeLight(Direction.NORTH, LightState.GREEN)
                );
        assertEquals("System is paused", exception.getMessage());
    }

    @Test
    void testConflictValidationNorthSouth() {
        service.changeLight(Direction.EAST, LightState.GREEN);
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        service.changeLight(Direction.NORTH, LightState.GREEN)
                );
        assertEquals("Conflict detected EAST/WEST GREEN", exception.getMessage());
    }

    @Test
    void testConflictValidationEastWest() {
        service.changeLight(Direction.NORTH, LightState.GREEN);
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () ->
                        service.changeLight(Direction.EAST, LightState.GREEN)
                );
        assertEquals("Conflict detected NORTH/SOUTH GREEN", exception.getMessage());
    }

    @Test
    void testGetCurrentState() {
        when(mapper.toTrafficStateDTO(any()))
                .thenReturn(new TrafficStateResponseDTO());
        List<TrafficStateResponseDTO> result = service.getCurrentState();
        assertEquals(4, result.size());
        verify(mapper, times(4))
                .toTrafficStateDTO(any());
    }

    @Test
    void testGetHistory() {
        when(mapper.toHistoryDTO(any()))
                .thenReturn(new HistoryResponseDTO("test", System.currentTimeMillis()));
        List<HistoryResponseDTO> history = service.getHistory();
        assertFalse(history.isEmpty());
        verify(mapper, atLeastOnce())
                .toHistoryDTO(any());
    }
}

