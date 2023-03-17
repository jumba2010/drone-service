package jumba.com.droneservice.repository;

import jumba.com.droneservice.domain.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findAllByIdIn(List<Long> medicationIds);
}
