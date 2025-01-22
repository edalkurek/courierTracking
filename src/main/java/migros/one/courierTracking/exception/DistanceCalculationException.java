package migros.one.courierTracking.exception;

public class DistanceCalculationException extends RuntimeException {
    public DistanceCalculationException(String details) {
        super("Error calculating distance: " + details);
    }
}
