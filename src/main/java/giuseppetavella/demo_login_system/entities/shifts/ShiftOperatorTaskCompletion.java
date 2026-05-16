package giuseppetavella.demo_login_system.entities.shifts;

import giuseppetavella.demo_login_system.entities.checklists.ChecklistEntry;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "shift_operator_tasks_completion",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"shift_operator_id", "checklist_entry_id"})
        }
)
public class ShiftOperatorTaskCompletion {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "shift_operator_id", nullable = false)
    private ShiftOperator shiftOperator;
    
    @ManyToOne
    @JoinColumn(name = "checklist_entry_id", nullable = false)
    private ChecklistEntry checklistEntry;
    
    @Column(nullable = false)
    private Boolean completed;
    
    @Column(name = "completed_at", nullable = false)
    private OffsetDateTime completedAt; 
    
    protected ShiftOperatorTaskCompletion() {}
    
    public ShiftOperatorTaskCompletion(ShiftOperator shiftOperator,
                                       ChecklistEntry checklistEntry) 
    {
        this.shiftOperator = shiftOperator;
        this.checklistEntry = checklistEntry;
        this.completed = true;
        this.completedAt = OffsetDateTime.now();
    }

    public ChecklistEntry getChecklistEntry() {
        return checklistEntry;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public OffsetDateTime getCompletedAt() {
        return completedAt;
    }

    public ShiftOperator getShiftOperator() {
        return shiftOperator;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ShiftOperatorTaskCompletion{" +
                "checklistEntry=" + checklistEntry +
                ", id=" + id +
                ", shiftOperator=" + shiftOperator +
                ", completed=" + completed +
                ", completedAt=" + completedAt +
                '}';
    }
}
