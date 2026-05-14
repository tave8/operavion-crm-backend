package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.checklists.ChecklistEntry;

import java.util.UUID;

public class ChecklistEntryToSendDTO {
    
    private final UUID id;
    private final UUID taskId;
    private final UUID checklistId;
    private final String taskName;
    private final int position;
    
    public ChecklistEntryToSendDTO(ChecklistEntry checklistEntry) 
    {
    
        this.id = checklistEntry.getId();
        this.taskId = checklistEntry.getTask().getId();
        this.checklistId = checklistEntry.getChecklist().getId();
        this.taskName = checklistEntry.getTask().getName();
        this.position = checklistEntry.getPosition();
        
    }

    public UUID getChecklistId() {
        return checklistId;
    }

    public UUID getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }
}
