package giuseppetavella.zero_chiamate.domain.entities.shifts.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.checklists.dto.to_send.ChecklistToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.to_send.ClientAddressToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.shifts.Shift;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.shift_days.dto.to_send.ShiftDayToSendDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class ShiftToSendDTO {
    
    private final UUID id;
    private final String name; 
    private final ClientAddressToSendDTO clientAddress;
    private final ChecklistToSendDTO checklist;
    private final List<ShiftDayToSendDTO> days;
    private final List<ProfileToSendDTO> operators;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    
    public ShiftToSendDTO(Shift shift, 
                          List<ShiftDayToSendDTO> days,
                          ClientAddressToSendDTO clientAddress,
                          ChecklistToSendDTO checklist,
                          String shiftName,
                          List<ProfileToSendDTO> operators) 
    {
    
        this.id = shift.getId();
        this.name = shiftName;
        this.clientAddress = clientAddress;
        this.checklist = checklist;
        this.days = days;
        this.operators = operators;
        this.startDate = shift.getStartDate();
        this.endDate = shift.getActualEndDate();
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

    public List<ProfileToSendDTO> getOperators() {
        return operators;
    }

    public String getName() {
        return name;
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
