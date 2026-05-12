package giuseppetavella.demo_login_system.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "checklist_entries")
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
    
    protected ChecklistEntry() {}
    
    public ChecklistEntry(Checklist checklist, Task task) 
    {
        
        this.checklist = checklist;
        this.task = task;
        
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

    @Override
    public String toString() {
        return "ChecklistEntry{" +
                "checklist=" + checklist +
                ", id=" + id +
                ", task=" + task +
                '}';
    }
}
