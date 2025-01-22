package migros.one.courierTracking.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class StoreVisitCountsResponse extends BaseResponse {
    private Map<String, Integer> visitCounts;

    public StoreVisitCountsResponse(String message, boolean success, Map<String, Integer> visitCounts) {
        super(message, success);
        this.visitCounts = visitCounts;
    }
}
