package migros.one.courierTracking.service;

import migros.one.courierTracking.dto.CourierLocationRequest;
import migros.one.courierTracking.dto.CourierLocationUpdateResponse;
import migros.one.courierTracking.dto.TotalTravelDistanceResponse;

import java.util.Map;

public interface CourierTrackerService {

    CourierLocationUpdateResponse updateLocation(CourierLocationRequest request);
    TotalTravelDistanceResponse getTotalTravelDistance(Long courierId, String unit);
}
