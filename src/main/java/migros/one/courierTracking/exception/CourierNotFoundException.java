package migros.one.courierTracking.exception;

public class CourierNotFoundException extends RuntimeException {
    public CourierNotFoundException(Long courierId) {
        super("Courier not found with ID: " + courierId);
    }
}
