package giuseppetavella.zero_chiamate.domain.entities.tasks.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.tasks.Task;

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
