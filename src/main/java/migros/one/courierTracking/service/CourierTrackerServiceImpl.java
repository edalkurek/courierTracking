package migros.one.courierTracking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import migros.one.courierTracking.dto.CourierLocationRequest;
import migros.one.courierTracking.dto.CourierLocationUpdateResponse;
import migros.one.courierTracking.dto.TotalTravelDistanceResponse;
import migros.one.courierTracking.exception.CourierNotFoundException;
import migros.one.courierTracking.exception.StoreNotFoundException;
import migros.one.courierTracking.model.Courier;
import migros.one.courierTracking.model.Location;
import migros.one.courierTracking.model.Store;
import migros.one.courierTracking.repository.CourierRepository;
import migros.one.courierTracking.strategy.DistanceInKilometers;
import migros.one.courierTracking.strategy.DistanceInMeters;
import migros.one.courierTracking.strategy.DistanceStrategy;
import migros.one.courierTracking.util.DistanceCalculator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierTrackerServiceImpl implements CourierTrackerService {

    private final CourierRepository courierRepository;
    private final StoreService storeService;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public CourierLocationUpdateResponse updateLocation(CourierLocationRequest request) {
        Courier inputCourier = modelMapper.map(request, Courier.class);
        LocalDateTime requestDate = request.getTime();

        StringBuilder responseMessage = new StringBuilder("Location updated successfully.");


        Courier courier = courierRepository.findByCourierId(request.getCourierId())
                .orElseGet(() -> {
                    Courier newCourier = Courier.builder()
                            .courierId(inputCourier.getCourierId())
                            .location(inputCourier.getLocation())
                            .totalDistance(0.0)
                            .enteredDate(null)
                            .build();

                    courierRepository.save(newCourier);
                    responseMessage.append(" New courier created successfully.");
                    return newCourier;
                });

        updateTotalDistance(courier, courier.getLocation(), inputCourier.getLocation());

        String proximityMessage = checkAndLogStoreProximity(courier, inputCourier.getLocation(), requestDate);
        if (!proximityMessage.isEmpty()) {
            responseMessage.append(" ").append(proximityMessage);
        }

        courier.setLocation(inputCourier.getLocation());
        courierRepository.save(courier);

        log.info("Response message for courier ID {}: {}", request.getCourierId(), responseMessage);
        return new CourierLocationUpdateResponse(request.getCourierId(), responseMessage.toString(), true);
    }

    private void updateTotalDistance(Courier courier, Location previousLocation, Location currentLocation) {
        if (previousLocation != null) {
            double distance = DistanceCalculator.calculateDistance(
                    previousLocation.getLat(),
                    previousLocation.getLng(),
                    currentLocation.getLat(),
                    currentLocation.getLng()
            );
            courier.setTotalDistance(courier.getTotalDistance() + distance);
            log.debug("Updated total distance for courier ID: {}. New total distance: {}", courier.getCourierId(), courier.getTotalDistance());
        }
    }

    private String checkAndLogStoreProximity(Courier courier, Location currentLocation, LocalDateTime requestDate) {
        List<Store> stores = storeService.getAllStores();

        if (stores.isEmpty()) {
            log.error("No stores found in the database.");
            throw new StoreNotFoundException();
        }

        for (Store store : stores) {
            double distanceToStore = DistanceCalculator.calculateDistance(
                    currentLocation.getLat(),
                    currentLocation.getLng(),
                    store.getLocation().getLat(),
                    store.getLocation().getLng()
            );

            if (distanceToStore <= 100) {
                if (isRecentStoreVisit(courier, store, requestDate)) {
                    log.info("Courier ID: {} revisited the store '{}' within 1 minute.", courier.getCourierId(), store.getName());
                    return "Courier revisited the store '" + store.getName() + "' within 1 minute.";
                } else {
                    log.info("Courier ID: {} entered store: {} at {}", courier.getCourierId(), store.getName(), requestDate);
                    storeService.logStoreVisit(courier, store, requestDate);
                    courier.setEnteredDate(requestDate);
                    courier.setStoreEntity(store);
                    return "Courier entered the store '" + store.getName() + "'.";
                }
            }
        }

        return "";
    }


    private boolean isRecentStoreVisit(Courier courier, Store store, LocalDateTime requestDate) {
        if (courier.getStoreEntity() == null || courier.getEnteredDate() == null) {
            return false;
        }
        return courier.getStoreEntity().equals(store)
                && courier.getEnteredDate().isAfter(requestDate.minusMinutes(1));
    }


    @Override
    public TotalTravelDistanceResponse getTotalTravelDistance(Long courierId, String unit) {
        DistanceStrategy strategy = "kilometers".equalsIgnoreCase(unit)
                ? new DistanceInKilometers()
                : new DistanceInMeters();

        return courierRepository.findByCourierId(courierId)
                .map(courier -> {
                    double totalDistanceInMeters = courier.getTotalDistance();
                    log.info("Total travel distance for courier ID: {}: {} meters", courierId, totalDistanceInMeters);

                    double convertedDistance = strategy.convertDistance(totalDistanceInMeters);

                    return new TotalTravelDistanceResponse(
                            "Total travel distance retrieved successfully.",
                            true,
                            convertedDistance,
                            unit
                    );
                })
                .orElseThrow(() -> {
                    log.error("Courier ID: {} not found.", courierId);
                    return new CourierNotFoundException(courierId);
                });
    }

}
