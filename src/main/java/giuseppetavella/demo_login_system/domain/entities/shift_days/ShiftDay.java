package giuseppetavella.demo_login_system.domain.entities.shift_days;

import giuseppetavella.demo_login_system.domain.entities.shifts.Shift;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.util.UUID;

@Entity
@Table(
        name = "shift_days",
        // a shift can have multiple days,
        // however for the same shift, the day is unique
        uniqueConstraints = @UniqueConstraint(columnNames = {"shift_id", "day"})
)
public class ShiftDay {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    
    protected ShiftDay() {}
    
    public ShiftDay(Shift shift, DayOfWeek day) 
    {

        this.setShift(shift);
        this.setDay(day);
        
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public UUID getId() {
        return id;
    }


    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "ShiftDay{" +
                "day=" + day +
                ", id=" + id +
                ", shift=" + shift +
                '}';
    }
}
