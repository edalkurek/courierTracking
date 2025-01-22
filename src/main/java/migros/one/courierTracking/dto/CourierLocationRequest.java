package migros.one.courierTracking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierLocationRequest {

    @NotNull(message = "Courier ID cannot be null")
    private Long courierId;

    @NotNull(message = "Location cannot be null")
    private Double lat;

    @NotNull(message = "Location cannot be null")
    private Double lng;

    @FutureOrPresent
    @NotNull(message = "Time cannot be null")
    private LocalDateTime time;
}
