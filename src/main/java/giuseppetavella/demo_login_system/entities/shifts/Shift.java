package giuseppetavella.demo_login_system.entities.shifts;

import giuseppetavella.demo_login_system.entities.checklists.Checklist;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.exceptions.ShiftException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.UUID;

@Entity
@Table(name = "shifts")
public class Shift {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "client_address_id", nullable = false)
    private ClientAddress clientAddress;
    
    @ManyToOne
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    // a shift can have no end date
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    protected Shift() {}
    
    public Shift(ClientAddress clientAddress, 
                 Checklist checklist,
                 LocalDate startDate,
                 LocalDate endDate,
                 LocalTime startTime,
                 LocalTime endTime) 
    {

        this.setClientAddress(clientAddress);
        this.setChecklist(checklist);
        
        // NOTE: for the setters validation to work properly
        // on initialization, first you must set end, then start
        this.setEndDate(endDate);
        this.setStartDate(startDate);
        
        this.setEndTime(endTime);
        this.setStartTime(startTime);
        
    }
    

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public ClientAddress getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(ClientAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

   
    public LocalTime getEndTime() {
        return endTime;
    }



    public UUID getId() {
        return id;
    }
    

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setEndTime(LocalTime endTime) {
        // no validation needed — endTime is the upper bound
        this.endTime = endTime;
    }

    public void setStartTime(LocalTime startTime) {
        boolean endTimeIsSet = this.endTime != null;
        boolean startTimeIsAfterEndTime = endTimeIsSet && startTime.isAfter(this.endTime);

        if (startTimeIsAfterEndTime) {
            throw new ShiftException("startTime must be before or equal to endTime");
        }
        this.startTime = startTime;
    }

    public void setEndDate(LocalDate endDate) {
        boolean endDateIsNotIndefinite = endDate != null;
        boolean startDateIsSet = this.startDate != null;
        boolean endDateIsBeforeStartDate = endDateIsNotIndefinite && startDateIsSet && endDate.isBefore(this.startDate);

        if (endDateIsBeforeStartDate) {
            throw new ShiftException("endDate must be after or equal to startDate");
        }
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        boolean endDateIsSet = this.endDate != null;
        boolean startDateIsNotNull = startDate != null;
        boolean startDateIsAfterEndDate = startDateIsNotNull && endDateIsSet && startDate.isAfter(this.endDate);

        if (startDateIsAfterEndDate) {
            throw new ShiftException("startDate must be before or equal to endDate");
        }
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }



    @Override
    public String toString() {
        return "Shift{" +
                "checklist=" + checklist +
                ", id=" + id +
                ", clientAddress=" + clientAddress +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
