package migros.one.courierTracking.service;

import migros.one.courierTracking.dto.StoreVisitCountsResponse;
import migros.one.courierTracking.model.Courier;
import migros.one.courierTracking.model.Store;

import java.time.LocalDateTime;
import java.util.List;

public interface StoreService {
    StoreVisitCountsResponse getStoreVisitCounts(Long courierId);
    void logStoreVisit(Courier courier, Store store, LocalDateTime requestDate);
    List<Store> getAllStores();
}
