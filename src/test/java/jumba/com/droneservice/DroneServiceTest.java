package jumba.com.droneservice;

import jumba.com.droneservice.domain.Drone;
import jumba.com.droneservice.domain.DroneModel;
import jumba.com.droneservice.domain.DroneState;
import jumba.com.droneservice.domain.Medication;
import jumba.com.droneservice.exceptions.BusinessException;
import jumba.com.droneservice.exceptions.EntityNotFoundException;
import jumba.com.droneservice.repository.DeliveryRepository;
import jumba.com.droneservice.repository.DroneRepository;
import jumba.com.droneservice.repository.MedicationRepository;
import jumba.com.droneservice.services.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

    private static final String SERIAL = "SN001";

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DroneService droneService;

    private Drone drone;

    @BeforeEach
    void setUp() {
        drone = new Drone();
        drone.setId(100L);
        drone.setSerialNumber(SERIAL);
        drone.setModel(DroneModel.LIGHTWEIGHT);
        drone.setWeightLimit(300);
        drone.setBatteryCapacity(100);
        drone.setState(DroneState.IDLE);
    }

    @Test
    @DisplayName("Loading an idle, charged drone within its weight limit assigns medications and marks it LOADED")
    void loadsDroneAndLinksBothSidesOfTheAssociation() {
        Medication paracetamol = medication(1L, 100);
        Medication ibuprofen = medication(2L, 150);
        when(droneRepository.findBySerialNumber(SERIAL)).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(paracetamol, ibuprofen));

        droneService.loadDroneWithMedications(SERIAL, List.of(1L, 2L));

        assertThat(drone.getState()).isEqualTo(DroneState.LOADED);
        assertThat(drone.getLoadedMedications()).containsExactly(paracetamol, ibuprofen);
        assertThat(paracetamol.getDrone()).isSameAs(drone);
        assertThat(ibuprofen.getDrone()).isSameAs(drone);
    }

    @Test
    void rejectsLoadWhenTotalWeightExceedsDroneLimit() {
        when(droneRepository.findBySerialNumber(SERIAL)).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByIdIn(List.of(1L, 2L)))
                .thenReturn(List.of(medication(1L, 200), medication(2L, 150)));

        assertThatThrownBy(() -> droneService.loadDroneWithMedications(SERIAL, List.of(1L, 2L)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("weight");
        assertThat(drone.getState()).isEqualTo(DroneState.IDLE);
    }

    @Test
    void rejectsLoadWhenBatteryIsBelowThreshold() {
        drone.setBatteryCapacity(24);
        when(droneRepository.findBySerialNumber(SERIAL)).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByIdIn(List.of(1L))).thenReturn(List.of(medication(1L, 10)));

        assertThatThrownBy(() -> droneService.loadDroneWithMedications(SERIAL, List.of(1L)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("battery");
    }

    @Test
    void rejectsLoadWhenDroneIsNotIdle() {
        drone.setState(DroneState.DELIVERING);
        when(droneRepository.findBySerialNumber(SERIAL)).thenReturn(Optional.of(drone));

        assertThatThrownBy(() -> droneService.loadDroneWithMedications(SERIAL, List.of(1L)))
                .isInstanceOf(BusinessException.class);
        verify(medicationRepository, never()).findAllByIdIn(anyList());
    }

    @Test
    void rejectsLoadWhenSomeMedicationsDoNotExist() {
        when(droneRepository.findBySerialNumber(SERIAL)).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByIdIn(List.of(1L, 99L))).thenReturn(List.of(medication(1L, 10)));

        assertThatThrownBy(() -> droneService.loadDroneWithMedications(SERIAL, List.of(1L, 99L)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void failsWithNotFoundForUnknownDrone() {
        when(droneRepository.findBySerialNumber("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> droneService.getLoadedMedicationsForDrone("UNKNOWN"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Unloading every medication records deliveries, detaches them and returns the drone to IDLE")
    void unloadingAllMedicationsReturnsDroneToIdle() {
        Medication paracetamol = medication(1L, 100);
        drone.loadMedications(List.of(paracetamol));
        drone.setState(DroneState.LOADED);
        when(droneRepository.findBySerialNumber(SERIAL)).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByIdIn(List.of(1L))).thenReturn(List.of(paracetamol));

        droneService.unloadMedicationsFromDrone(SERIAL, List.of(1L));

        assertThat(drone.getLoadedMedications()).isEmpty();
        assertThat(paracetamol.getDrone()).isNull();
        assertThat(drone.getState()).isEqualTo(DroneState.IDLE);
        verify(deliveryRepository).saveAll(anyList());
    }

    @Test
    void partialUnloadKeepsDroneDelivering() {
        Medication paracetamol = medication(1L, 100);
        Medication ibuprofen = medication(2L, 100);
        drone.loadMedications(List.of(paracetamol, ibuprofen));
        drone.setState(DroneState.LOADED);
        when(droneRepository.findBySerialNumber(SERIAL)).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByIdIn(List.of(1L))).thenReturn(List.of(paracetamol));

        droneService.unloadMedicationsFromDrone(SERIAL, List.of(1L));

        assertThat(drone.getLoadedMedications()).containsExactly(ibuprofen);
        assertThat(drone.getState()).isEqualTo(DroneState.DELIVERING);
    }

    private static Medication medication(Long id, double weight) {
        Medication medication = new Medication();
        medication.setId(id);
        medication.setCode("CODE" + id);
        medication.setName("Medication " + id);
        medication.setWeight(weight);
        return medication;
    }
}
