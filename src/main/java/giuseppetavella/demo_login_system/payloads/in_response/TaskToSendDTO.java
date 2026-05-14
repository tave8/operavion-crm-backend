package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.checklists.Task;

import java.util.UUID;

public class TaskToSendDTO {
    
    private final UUID taskId;
    private final String name;
    
    public TaskToSendDTO(Task task) {
        this.taskId = task.getId();
        this.name = task.getName();
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }
}
