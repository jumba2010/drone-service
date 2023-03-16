package jumba.com.droneservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jumba.com.droneservice.utils.BusinessConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Drone {

    @Id
    @Size(max = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @Max(500)
    private double weightLimit;

    @Min(BusinessConstants.MIN_BATTERY_CAPACITY) @Max(BusinessConstants.MAX_BATTERY_CAPACITY)
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    private DroneState state;

    @OneToMany(mappedBy = "drone")
    private List<Medication> loadedMedications=new ArrayList<>();

    public void loadMedications(List<Medication> medications) {
        medications.stream()
                .peek(medication -> medication.setDrone(this))
                .forEach(loadedMedications::add);
    }
}

