package giuseppetavella.demo_login_system.domain.entities.tasks.dto.to_send;

import giuseppetavella.demo_login_system.domain.entities.tasks.Task;

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
