package jumba.com.droneservice.repository;

import jumba.com.droneservice.domain.Drone;
import jumba.com.droneservice.domain.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, String> {

    List<Drone> findByState(DroneState droneState);
}