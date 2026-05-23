package giuseppetavella.demo_login_system.domain.entities.shift_days.dto.to_send;

import giuseppetavella.demo_login_system.domain.entities.shift_days.ShiftDay;

import java.time.DayOfWeek;
import java.util.UUID;

public class ShiftDayToSendDTO {
    
    private final UUID id;
    private final UUID shiftId;
    private final DayOfWeek day;
    
    public ShiftDayToSendDTO(ShiftDay shiftDay) {
        this.id = shiftDay.getId();
        this.shiftId = shiftDay.getShift().getId();
        this.day = shiftDay.getDay();
    }

    public DayOfWeek getDay() {
        return day;
    }

    public UUID getId() {
        return id;
    }

    public UUID getShiftId() {
        return shiftId;
    }
}
