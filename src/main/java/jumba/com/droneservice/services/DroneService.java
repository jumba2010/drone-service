package jumba.com.droneservice.services;

import jumba.com.droneservice.domain.Delivery;
import jumba.com.droneservice.domain.DeliveryStatus;
import jumba.com.droneservice.domain.Drone;
import jumba.com.droneservice.domain.DroneState;
import jumba.com.droneservice.domain.Medication;
import jumba.com.droneservice.exceptions.BusinessException;
import jumba.com.droneservice.exceptions.EntityNotFoundException;
import jumba.com.droneservice.repository.DeliveryRepository;
import jumba.com.droneservice.repository.DroneRepository;
import jumba.com.droneservice.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**

 Service class for managing Drone-related operations
 */
@Service
@RequiredArgsConstructor
public class DroneService {


    private final DroneRepository droneRepository;

    private final MedicationRepository medicationRepository;

    private final DeliveryRepository deliveryRepository;

    /**

     Registers a new drone in the system
     @param drone the drone to be registered
     @return the registered drone
     */
    public Drone registerDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    /**

     Retrieves a list of available drones that can be loaded with medications
     @return a list of available drones
     */
    public List<Drone> getAvailableDronesForLoading() {
        return droneRepository.findByState(DroneState.IDLE);
    }

    /**

     Retrieves a list of all medications available in the system
     @return a list of all medications
     */
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }


    /**
     Loads a drone with a list of medications and updates the drone's state to LOADED

     @param droneSerialNumber the serial number of the drone to be loaded

     @param medicationIds the list of medication IDs to be loaded onto the drone

     @throws BusinessException if there is an error during the loading process
     */
    public void loadDroneWithMedications(String droneSerialNumber, ArrayList<Long> medicationIds) throws BusinessException {
        Drone drone = droneRepository.findBySerialNumber(droneSerialNumber).orElseThrow(
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


    /**

     Unloads medications from a drone and updates the drone's state to IDLE when it has no medications left

     @param droneSerialNumber the serial number of the drone to be unloaded

     @param medicationIds the list of medication IDs to be unloaded from the drone

     @throws BusinessException if there is an error during the unloading process
     */
    public void unloadMedicationsFromDrone(String droneSerialNumber, List<Long> medicationIds) throws BusinessException {
        Drone drone = droneRepository.findBySerialNumber(droneSerialNumber).orElseThrow(
                () -> new EntityNotFoundException("Drone not found"));

        DroneValidator.validateIdleDroneState(drone);

        drone.setState(DroneState.DELIVERING);
        droneRepository.save(drone);

        deliveryMedications(medicationIds, drone);

        drone.removeMedications(medicationIds);


        // Update the drone's state to IDLE when it has no medications, this means the drone will be in the
        // Delivering status until all the medications are loaded
        if (drone.getLoadedMedications().isEmpty()) {
            drone.setState(DroneState.IDLE);
        }

        droneRepository.save(drone);
    }

    /**
     Creates delivery records for a list of medications and saves them to the system
     @param medicationIds the list of medication IDs to be delivered
     @param drone the drone responsible for the delivery
     */
    private void deliveryMedications(List<Long> medicationIds, Drone drone) {
        List<Medication> medications = medicationRepository.findAllByIdIn(medicationIds);
        List<Delivery> deliveries = medications.stream().map(medication ->
                        Delivery.builder().medication(medication)
                                .deliveryDate(LocalDateTime.now())
                                .status(DeliveryStatus.COMPLETED)
                                .drone(drone).build())
                .collect(Collectors.toList());
        deliveryRepository.saveAll(deliveries);
    }


/**

 Returns a list of medications currently loaded on the drone identified by the given serial number.
 @param droneSerialNumber The serial number of the drone to retrieve the loaded medications for.
 @return A list of medications currently loaded on the drone.
 @throws BusinessException If the drone identified by the given serial number is not found */
    public List<Medication> getLoadedMedicationsForDrone(String droneSerialNumber) throws BusinessException {
        Drone drone = droneRepository.findBySerialNumber(droneSerialNumber).orElseThrow(
                () -> new EntityNotFoundException("Drone not found"));

        if (drone.getState() != DroneState.LOADED && drone.getState() != DroneState.DELIVERING) {
            throw new BusinessException("Drone is not loaded or delivering and has no medications on board");
        }

        return drone.getLoadedMedications();
    }

}







