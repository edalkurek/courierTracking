package migros.one.courierTracking.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String details) {
        super("Invalid request: " + details);
    }
}