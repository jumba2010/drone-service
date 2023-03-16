package jumba.com.droneservice.repository;

import jumba.com.droneservice.domain.DroneHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneHistoryRepository extends JpaRepository<DroneHistory,Long> {
}
