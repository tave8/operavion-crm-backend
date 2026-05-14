package giuseppetavella.demo_login_system.entities.shifts;

import giuseppetavella.demo_login_system.entities.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "shift_operators",
        // the same operator can be assigned to the same shift only once
        uniqueConstraints = @UniqueConstraint(columnNames = {"shift_id", "operator_id"})
)
public class ShiftOperator {
    
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = false)
    private User operator;
    
    protected ShiftOperator() {}
    
    public ShiftOperator(Shift shift, User operator) {
        this.setShift(shift);
        this.setOperator(operator);
    }

    public UUID getId() {
        return id;
    }
    

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "ShiftOperator{" +
                "id=" + id +
                ", shift=" + shift +
                ", operator=" + operator +
                '}';
    }
}
