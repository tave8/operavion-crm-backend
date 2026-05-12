package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Task;
import giuseppetavella.demo_login_system.repositories.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TasksService {
    
    @Autowired
    private TasksRepository tasksRepository;

    /**
     * Save a task to DB.
     * @return
     */
    public Task save(Task task) 
    {
        return this.tasksRepository.save(task);
    }
    
    
}
