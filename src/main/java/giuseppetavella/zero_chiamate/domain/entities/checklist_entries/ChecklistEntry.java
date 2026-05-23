package giuseppetavella.zero_chiamate.domain.entities.checklist_entries;

import giuseppetavella.zero_chiamate.domain.entities.checklists.Checklist;
import giuseppetavella.zero_chiamate.domain.entities.tasks.Task;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "checklist_entries",
        uniqueConstraints = {
                // for the same checklist, the positions must be unique
                @UniqueConstraint(columnNames = {"checklist_id", "position"})
        }
)
public class ChecklistEntry {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;
    
    // position indicates the position/index 
    // of this checklist entry for the checklist it belongs to 
    @Column(nullable = false)
    private Integer position;
    
    protected ChecklistEntry() {}
    
    public ChecklistEntry(Checklist checklist, Task task, Integer position) 
    {
        
        this.checklist = checklist;
        this.task = task;
        this.position = position;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public UUID getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public Integer getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "ChecklistEntry{" +
                "checklist=" + checklist +
                ", id=" + id +
                ", task=" + task +
                '}';
    }
}
