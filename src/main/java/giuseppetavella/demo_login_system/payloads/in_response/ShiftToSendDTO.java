package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.shifts.Shift;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.List;
import java.util.UUID;

public class ShiftToSendDTO {
    
    private final UUID id;
    private final ClientAddressToSendDTO clientAddress;
    private final ChecklistToSendDTO checklist;
    private final List<ShiftDayToSendDTO> days;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    
    public ShiftToSendDTO(Shift shift, 
                          List<ShiftDayToSendDTO> days,
                          ClientAddressToSendDTO clientAddress,
                          ChecklistToSendDTO checklist) 
    {
    
        this.id = shift.getId();
        this.clientAddress = clientAddress;
        this.checklist = checklist;
        this.days = days;
        this.startDate = shift.getStartDate();
        this.endDate = shift.getEndDate();
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        
    }

    public ChecklistToSendDTO getChecklist() {
        return checklist;
    }

    public ClientAddressToSendDTO getClientAddress() {
        return clientAddress;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<ShiftDayToSendDTO> getDays() {
        return days;
    }

    public UUID getId() {
        return id;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
}
