package jumba.com.droneservice.repository;

import jumba.com.droneservice.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}