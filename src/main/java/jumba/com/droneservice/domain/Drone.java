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

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model",nullable = false)
    @NotNull
    private DroneModel model;

    @Positive
    @DecimalMax(value = "500", message = "weight limit must not exceed 500 grams")
    @Column(name = "weight_limit", nullable = false)
    private double weightLimit;

    @Min(BusinessConstants.MIN_BATTERY_CAPACITY) @Max(BusinessConstants.MAX_BATTERY_CAPACITY)
    @Column(name = "battery_capacity",nullable = false)
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private DroneState state = DroneState.IDLE;

    // Excluded from equals/hashCode/toString to avoid infinite recursion through the
    // bidirectional Drone <-> Medication association and accidental lazy loading.
    @OneToMany(mappedBy = "drone")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Medication> loadedMedications = new ArrayList<>();

    /**
     * Assigns the medications to this drone, keeping both sides of the association in sync.
     * {@link Medication#getDrone()} is the owning side, so it is what Hibernate persists.
     */
    public void loadMedications(List<Medication> medications) {
        medications.forEach(medication -> {
            medication.setDrone(this);
            loadedMedications.add(medication);
        });
    }

    /**
     * Detaches the given medications from this drone on both sides of the association.
     */
    public void removeMedications(List<Long> medicationIds) {
        loadedMedications.removeIf(medication -> {
            boolean remove = medicationIds.contains(medication.getId());
            if (remove) {
                medication.setDrone(null);
            }
            return remove;
        });
    }
}

