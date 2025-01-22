package migros.one.courierTracking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseResponse {
    private String message;
    private boolean success;
    private LocalDateTime timestamp;

    public BaseResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.timestamp = LocalDateTime.now();
    }
}


