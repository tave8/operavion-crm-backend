package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.shifts.TaskCompletion;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TaskCompletionToSendDTO {
    
    private final UUID id;
    private final UUID shiftOperatorId;
    private final UUID checklistEntryId;
    private final boolean completed;
    private final OffsetDateTime completedAt;
    
    public TaskCompletionToSendDTO(TaskCompletion taskCompletion) {
        this.id = taskCompletion.getId();
        this.shiftOperatorId = taskCompletion.getShiftOperator().getId();
        this.checklistEntryId = taskCompletion.getChecklistEntry().getId();
        this.completed = taskCompletion.isCompleted();
        this.completedAt = taskCompletion.getCompletedAt();
    }

    public OffsetDateTime getCompletedAt() {
        return completedAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public UUID getChecklistEntryId() {
        return checklistEntryId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getShiftOperatorId() {
        return shiftOperatorId;
    }
}
