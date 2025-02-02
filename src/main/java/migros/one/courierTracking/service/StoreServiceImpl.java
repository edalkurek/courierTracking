package migros.one.courierTracking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import migros.one.courierTracking.dto.StoreVisitCountsResponse;
import migros.one.courierTracking.model.Courier;
import migros.one.courierTracking.model.Location;
import migros.one.courierTracking.model.Store;
import migros.one.courierTracking.model.VisitLog;
import migros.one.courierTracking.repository.StoreRepository;
import migros.one.courierTracking.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StoreServiceImpl implements StoreService{

    private final ObjectMapper objectMapper;
    private final StoreRepository storeRepository;
    private final VisitLogRepository visitLogRepository;


    @Autowired
    public StoreServiceImpl(ObjectMapper objectMapper, StoreRepository storeRepository, VisitLogRepository visitLogRepository) {
        this.objectMapper = objectMapper;
        this.storeRepository = storeRepository;
        this.visitLogRepository = visitLogRepository;
    }

    @PostConstruct
    public void initStoresFromJson() {
        syncStoresFromJson();
        log.info("Stores synced from JSON at application startup.");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void dailySyncStores() {
        syncStoresFromJson();
        log.info("Stores synced from JSON to database.");
    }

    @Transactional
    public void syncStoresFromJson() {
        try {
            var resource = new ClassPathResource("data/stores.json");
            List<Map<String, Object>> rawStores = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Map<String, Object>>>() {});

            // JSON verisini manuel olarak `Store` ve `Location` nesnesine dönüştürme
            List<Store> stores = rawStores.stream().map(rawStore -> {
                String name = (String) rawStore.get("name");
                double lat = (double) rawStore.get("lat");
                double lng = (double) rawStore.get("lng");

                // Location nesnesi oluştur
                Location location = Location.builder()
                        .lat(lat)
                        .lng(lng)
                        .build();

                // Store nesnesi oluştur
                return Store.builder()
                        .name(name)
                        .location(location)
                        .build();
            }).collect(Collectors.toList());

            // Veritabanına kaydetme veya güncelleme işlemi
            for (Store store : stores) {
                storeRepository.findByName(store.getName()).ifPresentOrElse(existingStore -> {
                    existingStore.setLocation(store.getLocation());
                    storeRepository.save(existingStore);
                }, () -> {
                    storeRepository.save(store);
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to sync stores from JSON", e);
        }
    }

    @Override
    public StoreVisitCountsResponse getStoreVisitCounts(Long courierId) {
        List<Object[]> results = visitLogRepository.findVisitCountsByCourierId(courierId);

        Map<String, Integer> visitCounts = new HashMap<>();
        for (Object[] result : results) {
            String storeName = (String) result[0];
            Integer visitCount = (Integer) result[1];
            visitCounts.put(storeName, visitCount);
        }

        String message = visitCounts.isEmpty()
                ? "No store visits found for the given courier ID."
                : "Store visit counts retrieved successfully.";

        return new StoreVisitCountsResponse(message, true, visitCounts);
    }



    @Transactional
    public void logStoreVisit(Courier courier, Store store, LocalDateTime requestDate) {
        VisitLog visitLog = visitLogRepository.findByCourierAndStore(courier, store)
                .orElseGet(() -> VisitLog.builder()
                        .courier(courier)
                        .store(store)
                        .visitCount(0)
                        .build());

        visitLog.setVisitCount(visitLog.getVisitCount() + 1);
        visitLog.setEntryTime(requestDate);
        visitLogRepository.save(visitLog);
    }

    @Override
    @Cacheable(value = "stores")
    public List<Store> getAllStores() {
        log.info("Fetching all stores from the database.");
        return storeRepository.findAll();
    }
}

