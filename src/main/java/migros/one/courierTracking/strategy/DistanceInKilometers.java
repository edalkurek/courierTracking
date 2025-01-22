package migros.one.courierTracking.strategy;

import migros.one.courierTracking.util.DistanceCalculator;

public class DistanceInKilometers implements DistanceStrategy {

    @Override
    public double convertDistance(double distanceInMeters) {
        return distanceInMeters / 1000;
    }
}
