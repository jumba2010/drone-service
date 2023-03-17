package jumba.com.droneservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jumba.com.droneservice.utils.BusinessConstants;
import lombok.Data;
import jakarta.persistence.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "serial_number",nullable = false)
    @NotNull
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model",nullable = false)
    @NotNull
    private DroneModel model;

    @Size(max = 500)
    @Column(name = "weight_limit",nullable = false)
    private double weightLimit;

    @Min(BusinessConstants.MIN_BATTERY_CAPACITY) @Max(BusinessConstants.MAX_BATTERY_CAPACITY)
    @Column(name = "battery_capacity",nullable = false)
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "state",nullable = false)
    @Size(max = 100)
    private DroneState state;

    @OneToMany(mappedBy = "drone")
    private List<Medication> loadedMedications=new ArrayList<>();

    public void loadMedications(List<Medication> medications) {
        medications.stream()
                .peek(medication -> medication.setDrone(this))
                .forEach(loadedMedications::add);
    }

    public void removeMedications(List<Long> medicationIds) {
        loadedMedications.removeIf(m -> medicationIds.contains(m.getId()));
    }
}

