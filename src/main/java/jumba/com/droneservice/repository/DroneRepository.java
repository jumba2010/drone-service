package jumba.com.droneservice.repository;

import jumba.com.droneservice.domain.Drone;
import jumba.com.droneservice.domain.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {

    List<Drone> findByState(DroneState droneState);

    Optional<Drone> findBySerialNumber(String droneSerialNumber);
}