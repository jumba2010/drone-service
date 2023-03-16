package jumba.com.droneservice.services;

import jumba.com.droneservice.domain.Drone;
import jumba.com.droneservice.domain.DroneState;
import jumba.com.droneservice.domain.Medication;
import jumba.com.droneservice.exceptions.BusinessException;
import jumba.com.droneservice.exceptions.EntityNotFoundException;
import jumba.com.droneservice.utils.BusinessConstants;

import java.util.List;

public class DroneValidator {
    public static void validateDroneState(Drone drone) {
        if (drone.getState() != DroneState.IDLE) {
            throw new BusinessException("Drone is not idle and cannot be loaded");
        }
    }

    public static void validateMedications(Drone drone,List<Medication> medications, List<Long> medicationIds) {
        if (medications.size() != medicationIds.size()) {
            throw new EntityNotFoundException("One or more medications not found");
        }

        double totalWeight = medications.stream().mapToDouble(Medication::getWeight).sum();
        if (totalWeight > drone.getWeightLimit()) {
            throw new BusinessException("Drone cannot be loaded with more weight than its limit");
        }
    }

    public static void validateDroneBattery(Drone drone) {
        if (drone.getBatteryCapacity() < BusinessConstants.LOW_BATTERY_CAPACITY) {
            throw new BusinessException("Drone cannot be loaded when battery level is below 25%");
        }
    }
}
