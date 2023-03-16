package jumba.com.droneservice.domain;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(appliesTo="drone_history")
@Builder
@Data
@AllArgsConstructor
public class DroneHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;

    @JoinColumn(name = "battery_capacity", nullable = false)
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneState state;

    @Enumerated(EnumType.STRING)
    private BatteryState batteryState;

    @Column(nullable = false)
    private LocalDateTime timestamp;

}
