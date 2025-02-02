package migros.one.courierTracking.repository;

import migros.one.courierTracking.model.Courier;
import migros.one.courierTracking.model.Store;
import migros.one.courierTracking.model.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    @Query("SELECT v.store.name, v.visitCount FROM VisitLog v WHERE v.courier.courierId = :courierId")
    List<Object[]> findVisitCountsByCourierId(@Param("courierId") Long courierId);

    Optional<VisitLog> findByCourierAndStore(Courier courier, Store store);

}
