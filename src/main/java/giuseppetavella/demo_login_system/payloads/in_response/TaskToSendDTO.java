package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.Task;

public class TaskToSendDTO {
    
    private final String name;
    
    public TaskToSendDTO(Task task) {
        this.name = task.getName();
    }

    public String getName() {
        return name;
    }
}
