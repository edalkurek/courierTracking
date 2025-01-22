package migros.one.courierTracking.service;

import migros.one.courierTracking.dto.TotalTravelDistanceResponse;
import migros.one.courierTracking.exception.CourierNotFoundException;
import migros.one.courierTracking.model.Courier;
import migros.one.courierTracking.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourierTrackerServiceImplTest {

    @InjectMocks
    private CourierTrackerServiceImpl courierTrackerService;

    @Mock
    private CourierRepository courierRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalTravelDistance_Success_Meters() {
        // Arrange
        Long courierId = 1L;
        String unit = "meters";
        Courier courier = new Courier();
        courier.setCourierId(courierId);
        courier.setTotalDistance(1000.0); // 1000 meters

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        TotalTravelDistanceResponse response = courierTrackerService.getTotalTravelDistance(courierId, unit);

        assertNotNull(response);
        assertEquals("Total travel distance retrieved successfully.", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(1000.0, response.getTotalDistance());
        assertEquals("meters", response.getUnit());

        verify(courierRepository, times(1)).findById(courierId);
    }

    @Test
    void testGetTotalTravelDistance_Success_Kilometers() {
        // Arrange
        Long courierId = 1L;
        String unit = "kilometers";
        Courier courier = new Courier();
        courier.setCourierId(courierId);
        courier.setTotalDistance(1000.0); // 1000 meters

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        TotalTravelDistanceResponse response = courierTrackerService.getTotalTravelDistance(courierId, unit);

        assertNotNull(response);
        assertEquals("Total travel distance retrieved successfully.", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(1.0, response.getTotalDistance()); // 1000 meters = 1 kilometer
        assertEquals("kilometers", response.getUnit());

        verify(courierRepository, times(1)).findById(courierId);
    }

    @Test
    void testGetTotalTravelDistance_CourierNotFound() {

        Long courierId = 1L;
        String unit = "meters";

        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        CourierNotFoundException exception = assertThrows(CourierNotFoundException.class,
                () -> courierTrackerService.getTotalTravelDistance(courierId, unit));

        assertEquals("Courier ID: 1 not found.", exception.getMessage());
        verify(courierRepository, times(1)).findById(courierId);
    }
}