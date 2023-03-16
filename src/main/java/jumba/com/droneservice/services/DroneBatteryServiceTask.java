package jumba.com.droneservice.services;

import jumba.com.droneservice.domain.BatteryState;
import jumba.com.droneservice.domain.Drone;
import jumba.com.droneservice.domain.DroneHistory;
import jumba.com.droneservice.domain.DroneState;
import jumba.com.droneservice.repository.DroneHistoryRepository;
import jumba.com.droneservice.repository.DroneRepository;
import jumba.com.droneservice.utils.BusinessConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DroneBatteryServiceTask {

    private final DroneRepository droneRepository;
    private final DroneHistoryRepository droneHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(DroneService.class);

    @Scheduled(fixedRate = 300000) // Run every 5 minutes (300000 milliseconds)
    public void checkDronesBatteryLevel() {
        List<Drone> drones = droneRepository.findAll();
        drones.stream()
                .forEach(drone -> {
                    if(drone.getBatteryCapacity() < BusinessConstants.LOW_BATTERY_CAPACITY){
                        logger.warn("Drone with serial number {} has low battery level: {}%", drone.getSerialNumber(),
                                drone.getBatteryCapacity());
                        drone.setState(DroneState.IDLE);
                        droneRepository.save(drone);
                    }

                    DroneHistory history =DroneHistory.builder()
                            .batteryCapacity(drone.getBatteryCapacity())
                            .drone(drone)
                            .state(drone.getState())
                            .batteryState(BatteryState.getState(drone.getBatteryCapacity())).build(); ;
                    droneHistoryRepository.save(history);
                });


    }

}
