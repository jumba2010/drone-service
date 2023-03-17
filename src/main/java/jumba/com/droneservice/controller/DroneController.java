package jumba.com.droneservice.controller;

import jakarta.validation.Valid;
import jumba.com.droneservice.domain.Drone;
import jumba.com.droneservice.domain.Medication;
import jumba.com.droneservice.exceptions.BusinessException;
import jumba.com.droneservice.request.MedicationRequest;
import jumba.com.droneservice.services.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drones")
@Validated
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping
    public ResponseEntity<Drone> registerDrone(@Valid @RequestBody Drone drone) {
        return ResponseEntity.ok(droneService.registerDrone(drone));
    }

    @GetMapping("/available-for-loading")
    public ResponseEntity<List<Drone>> getAvailableDronesForLoading() {
        return ResponseEntity.ok(droneService.getAvailableDronesForLoading());
    }

    @PostMapping("/{droneSerialNumber}/medications")
    public ResponseEntity<Void> loadDroneWithMedications(@PathVariable String droneSerialNumber,
            @Valid @RequestBody MedicationRequest medicationRequest) throws BusinessException {
        droneService.loadDroneWithMedications(droneSerialNumber, medicationRequest.getMedicationIds());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{droneSerialNumber}/medications")
    public ResponseEntity<Void> unloadMedicationsFromDrone(@PathVariable String droneSerialNumber, @RequestBody MedicationRequest medicationRequest) throws BusinessException {
        droneService.unloadMedicationsFromDrone(droneSerialNumber, medicationRequest.getMedicationIds());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/medications")
    public ResponseEntity<List<Medication>> getAllMedications() {
        return ResponseEntity.ok(droneService.getAllMedications());
    }

    @GetMapping("/{droneSerialNumber}/medications")
    public ResponseEntity<List<Medication>> getLoadedMedicationsForDrone(@PathVariable String droneSerialNumber) throws BusinessException {
        return ResponseEntity.ok(droneService.getLoadedMedicationsForDrone(droneSerialNumber));
    }
}
