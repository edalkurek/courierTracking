package migros.one.courierTracking.strategy;

import migros.one.courierTracking.util.DistanceCalculator;

public class DistanceInMeters implements DistanceStrategy {

    @Override
    public double convertDistance(double distanceInMeters) {
        return distanceInMeters;
    }
}
