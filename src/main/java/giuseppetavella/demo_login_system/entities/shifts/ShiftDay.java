package giuseppetavella.demo_login_system.entities.shifts;

import jakarta.persistence.*;

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
    private Integer day;
    
    protected ShiftDay() {}
    
    public ShiftDay(Shift shift, Integer day) 
    {

        this.setShift(shift);
        this.setDay(day);
        
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        // TODO: check that day is between 1 and 7
        if (day < 1 || day > 7) {
            throw new IllegalArgumentException("Day must be between 1 and 7");
        }
        
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
