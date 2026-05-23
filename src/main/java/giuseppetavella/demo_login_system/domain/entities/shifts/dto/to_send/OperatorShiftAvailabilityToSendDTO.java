package giuseppetavella.demo_login_system.domain.entities.shifts.dto.to_send;

import java.time.LocalDate;
import java.util.List;

public class OperatorShiftAvailabilityToSendDTO {
    
    private final LocalDate date;
    private final boolean available;
    private final List<ShiftToSendDTO> shifts;
    
    public OperatorShiftAvailabilityToSendDTO(boolean isAvailable,
                                              List<ShiftToSendDTO> shifts,
                                              LocalDate date) 
    {
        this.available = isAvailable;
        this.shifts = shifts;
        this.date = date;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<ShiftToSendDTO> getShifts() {
        return shifts;
    }
}
