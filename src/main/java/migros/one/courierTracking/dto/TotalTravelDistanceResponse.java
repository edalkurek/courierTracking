package migros.one.courierTracking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalTravelDistanceResponse extends BaseResponse {
    private double totalDistance;
    private String unit;

    public TotalTravelDistanceResponse(String message, boolean success, double totalDistance, String unit) {
        super(message, success);
        this.totalDistance = totalDistance;
        this.unit = unit;
    }
}
