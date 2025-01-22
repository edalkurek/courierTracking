package migros.one.courierTracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import migros.one.courierTracking.dto.CourierLocationRequest;
import migros.one.courierTracking.dto.CourierLocationUpdateResponse;
import migros.one.courierTracking.dto.StoreVisitCountsResponse;
import migros.one.courierTracking.dto.TotalTravelDistanceResponse;
import migros.one.courierTracking.service.CourierTrackerService;
import migros.one.courierTracking.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Courier Tracker API", description = "Kurye takip sistemi")
public class CourierTrackerController {

    private final CourierTrackerService courierService;
    private final StoreService storeService;

    public CourierTrackerController(CourierTrackerService courierService, StoreService storeService) {
        this.courierService = courierService;
        this.storeService = storeService;
    }

    @Operation(summary = "Kurye konumunu güncelle", description = "Bir kuryenin konumunu günceller.")
    @PostMapping("/location")
    public ResponseEntity<CourierLocationUpdateResponse> updateCourierLocation(@Valid @RequestBody CourierLocationRequest request) {
        CourierLocationUpdateResponse response = courierService.updateLocation(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Belirli bir courier için toplam seyahat mesafesini sorgular
     */
    @Operation(summary = "Toplam seyahat mesafesini getir", description = "Bir kuryenin toplam seyahat mesafesini birim bazında döndürür.")
    @GetMapping("/{courierId}/total-distance")
    public ResponseEntity<TotalTravelDistanceResponse> getTotalTravelDistance(
            @PathVariable Long courierId,
            @RequestParam(defaultValue = "meters") String unit) {

        TotalTravelDistanceResponse response = courierService.getTotalTravelDistance(courierId, unit);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mağaza ziyaret sayısını getir", description = "Bir kuryenin ziyaret ettiği mağazaların sayısını döndürür.")
    @GetMapping("/{courierId}/store-visit-counts")
    public ResponseEntity<StoreVisitCountsResponse> getStoreVisitCounts(@PathVariable Long courierId) {
        StoreVisitCountsResponse response = storeService.getStoreVisitCounts(courierId);
        return ResponseEntity.ok(response);
    }
}
