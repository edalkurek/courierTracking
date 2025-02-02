package migros.one.courierTracking.service;

import migros.one.courierTracking.dto.StoreVisitCountsResponse;
import migros.one.courierTracking.repository.VisitLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreServiceImplTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private VisitLogRepository visitLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStoreVisitCounts_Success() {
        // Arrange
        Long courierId = 1L;
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{"Ataşehir MMM Migros", 5});
        mockResults.add(new Object[]{"Kadıköy Migros", 3});

        when(visitLogRepository.findVisitCountsByCourierId(courierId)).thenReturn(mockResults);

        // Act
        StoreVisitCountsResponse response = storeService.getStoreVisitCounts(courierId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Store visit counts retrieved successfully.", response.getMessage());
        assertEquals(2, response.getVisitCounts().size());
        assertEquals(5, response.getVisitCounts().get("Ataşehir MMM Migros"));
        assertEquals(3, response.getVisitCounts().get("Kadıköy Migros"));

        verify(visitLogRepository, times(1)).findVisitCountsByCourierId(courierId);
    }

    @Test
    void testGetStoreVisitCounts_EmptyResults() {
        // Arrange
        Long courierId = 1L;
        when(visitLogRepository.findVisitCountsByCourierId(courierId)).thenReturn(new ArrayList<>());

        // Act
        StoreVisitCountsResponse response = storeService.getStoreVisitCounts(courierId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("No store visits found for the given courier ID.", response.getMessage());
        assertTrue(response.getVisitCounts().isEmpty());

        verify(visitLogRepository, times(1)).findVisitCountsByCourierId(courierId);
    }
}