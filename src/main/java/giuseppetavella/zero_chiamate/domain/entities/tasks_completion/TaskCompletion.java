package giuseppetavella.zero_chiamate.domain.entities.tasks_completion;

import giuseppetavella.zero_chiamate.domain.entities.shift_operators.ShiftOperator;
import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.ChecklistEntry;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "tasks_completion",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"shift_operator_id", "checklist_entry_id"})
        }
)
public class TaskCompletion {
    
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
    
    protected TaskCompletion() {}
    
    public TaskCompletion(ShiftOperator shiftOperator,
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

    public Boolean isCompleted() {
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
