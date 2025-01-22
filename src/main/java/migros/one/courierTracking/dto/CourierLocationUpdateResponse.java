package migros.one.courierTracking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourierLocationUpdateResponse extends BaseResponse {
    private Long courierId;

    public CourierLocationUpdateResponse(Long courierId, String message, boolean success) {
        super(message, success);
        this.courierId = courierId;
    }
}
