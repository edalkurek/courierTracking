package migros.one.courierTracking.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException() {
        super("No stores found in the database.");
    }
}
