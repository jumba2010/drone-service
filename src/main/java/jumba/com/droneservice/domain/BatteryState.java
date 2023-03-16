package jumba.com.droneservice.domain;

import jumba.com.droneservice.utils.BusinessConstants;

public enum BatteryState {
    LOW,
    IN_USE,
    FULL;

    public static BatteryState getState(int capacity) {
        if (capacity < BusinessConstants.LOW_BATTERY_CAPACITY) {
            return LOW;
        } else if (capacity < BusinessConstants.MAX_BATTERY_CAPACITY) {
            return IN_USE;
        } else {
            return FULL;
        }
    }
}

