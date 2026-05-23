package giuseppetavella.zero_chiamate.domain.entities.shifts;

import giuseppetavella.zero_chiamate.domain.entities.checklists.Checklist;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddress;
import giuseppetavella.zero_chiamate.exceptions.ShiftException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
    
    // a shift can have no end date, but we internally
    // don't store null; instead, we store a default distant future date
    @Column(name = "end_date", nullable = false)
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
        if(endTime == null) {
            throw new ShiftException("While setting endTime, endTime cannot be null.");
        }
        
        // no validation needed — endTime is the upper bound
        this.endTime = endTime;
    }

    public void setStartTime(LocalTime startTime) {
        if(startTime == null) {
            throw new ShiftException("While setting startTime, startTime cannot be null.");
        }
        
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
        
        // the end date was null, which means the end date is indefinite,
        // which means the value of end date is the default distant future date
        // user and frontend should know nothing about this
        if(endDate == null) {
            this.endDate = this.getDefaultDistantFutureDate();
        } 
        // an end date was provided
        else {
            this.endDate = endDate;
        }
        
    }

    public void setStartDate(LocalDate startDate) {
        if(startDate == null) {
            throw new ShiftException("While setting startDate, startDate cannot be null.");
        }
        
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



    /**
     * Return the actual end date if exists, otherwise null.
     * The actual end date is any date that is not the default distant future date.
     *
     * The default distant future date is a default date, 
     * that is the same on write and read, that gets internally 
     * set so as to signify "the end date is indefined".
     *
     * @return
     */
    public LocalDate getActualEndDate() {
        if(this.hasDefiniteEndDate()) {
            return this.getEndDate();
        }
        return null;
    }

    /**
     * Use this method when you need to know whether 
     * this shift has an end date. You need to call this method
     * to know, because the end date is not set to null in the DB.
     * Instead, we use a default distant future date, which 
     * must naturally be the same both on write and on read, 
     * so that we can know whether or not a shift has an end date,
     * without having a null in DB.
     * 
     * @return
     */
    public boolean hasDefiniteEndDate() {
        // this shift is said to have an end date,
        // if that end date is not the default distant future date
        return !this.hasIndefiniteEndDate();
    }

    /**
     * Is the end date of this shift the default distant future date?
     * This mechanism is used to void having endDate of shifts as nulls.
     *
     * */
     public boolean hasIndefiniteEndDate() {
        LocalDate endDate = this.getEndDate();
        LocalDate distantFutureDate = this.getDefaultDistantFutureDate();
        return endDate.equals(distantFutureDate);
    }
    

    /**
     * This is the default distant future date that is stored 
     * as end date, when the end date is indefinite, so we don't have
     * to store nulls in DB.
     * 
     * We should be able not to care what date it is.
     * For example it could be something like 3000-01-01
     * (first january of year 3000)
     * 
     * @return
     */
    public LocalDate getDefaultDistantFutureDate() {
        return LocalDate.of(3000, 1, 1);
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
