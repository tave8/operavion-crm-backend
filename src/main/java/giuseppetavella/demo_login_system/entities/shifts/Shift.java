package giuseppetavella.demo_login_system.entities.shifts;

import giuseppetavella.demo_login_system.entities.checklists.Checklist;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import jakarta.persistence.*;

import java.time.LocalDate;
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
    private OffsetTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private OffsetTime endTime;
    
    protected Shift() {}
    
    public Shift(ClientAddress clientAddress, 
                 Checklist checklist,
                 LocalDate startDate,
                 LocalDate endDate,
                 OffsetTime startTime,
                 OffsetTime endTime) 
    {
        // TODO: check that start date <= end date
        // TODO: check that start time <= end time
        this.setClientAddress(clientAddress);
        this.setChecklist(checklist);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setStartTime(startTime);
        this.setEndTime(endTime);   
        
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

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public OffsetTime getEndTime() {
        return endTime;
    }

    public void setEndTime(OffsetTime endTime) {
        this.endTime = endTime;
    }

    public UUID getId() {
        return id;
    }
    

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public OffsetTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetTime startTime) {
        this.startTime = startTime;
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
