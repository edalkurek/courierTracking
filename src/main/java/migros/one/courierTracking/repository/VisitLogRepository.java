package migros.one.courierTracking.repository;

import migros.one.courierTracking.model.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    @Query("SELECT v.store.name, COUNT(v) FROM VisitLog v WHERE v.courier.id = :courierId GROUP BY v.store.name")
    List<Object[]> findVisitCountsByCourierId(@Param("courierId") Long courierId);
}
