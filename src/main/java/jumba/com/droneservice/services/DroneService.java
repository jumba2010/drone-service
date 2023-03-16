package jumba.com.droneservice.services;

import jumba.com.droneservice.domain.Drone;
import jumba.com.droneservice.domain.DroneState;
import jumba.com.droneservice.domain.Medication;
import jumba.com.droneservice.exceptions.BusinessException;
import jumba.com.droneservice.exceptions.EntityNotFoundException;
import jumba.com.droneservice.repository.DroneRepository;
import jumba.com.droneservice.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DroneService {


    private final DroneRepository droneRepository;

    private final MedicationRepository medicationRepository;

    public Drone registerDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    public List<Drone> getAvailableDronesForLoading() {
        return droneRepository.findByState(DroneState.IDLE);
    }

    public void loadDroneWithMedications(String droneSerialNumber, List<Long> medicationIds) throws BusinessException {
        Drone drone = droneRepository.findById(droneSerialNumber).orElseThrow(
                () -> new EntityNotFoundException("Drone not found"));

        DroneValidator.validateDroneState(drone);

        List<Medication> medications = medicationRepository.findAllByIdIn(medicationIds);

        DroneValidator.validateMedications(drone, medications, medicationIds);

        DroneValidator.validateDroneBattery(drone);

        drone.setState(DroneState.LOADING);
        droneRepository.save(drone);

        // Load the medications onto the drone
        drone.loadMedications(medications);

        // Update the drone's state to LOADED
        drone.setState(DroneState.LOADED);
        droneRepository.save(drone);
    }


    public List<Medication> getLoadedMedicationsForDrone(String droneSerialNumber) throws BusinessException {
        Drone drone = droneRepository.findById(droneSerialNumber).orElseThrow(
                () -> new EntityNotFoundException("Drone not found"));

        if (drone.getState() != DroneState.LOADED && drone.getState() != DroneState.DELIVERING) {
            throw new BusinessException("Drone is not loaded or delivering and has no medications on board");
        }

        return drone.getLoadedMedications();
    }

}







