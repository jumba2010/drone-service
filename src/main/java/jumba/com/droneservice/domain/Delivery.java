package jumba.com.droneservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drone_serial_number")
    private Drone drone;

    @ManyToOne
    @JoinColumn(name = "medication_code")
    private Medication medication;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
