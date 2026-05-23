package giuseppetavella.demo_login_system.domain.entities.shifts.dto.to_send;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OperatorShiftConflictsToSendDTO {
    
    private final boolean hasConflicts;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final List<ShiftToSendDTO> shifts;
    
    
    public OperatorShiftConflictsToSendDTO(boolean hasConflicts,
                                           List<ShiftToSendDTO> shifts,
                                           LocalDate startDate,
                                           LocalDate endDate,
                                           LocalTime startTime,
                                           LocalTime endTime) 
    {
        this.hasConflicts = hasConflicts;
        this.shifts = shifts;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isHasConflicts() {
        return hasConflicts;
    }

    public List<ShiftToSendDTO> getShifts() {
        return shifts;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
