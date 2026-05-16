package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.shifts.TaskCompletion;
import giuseppetavella.demo_login_system.repositories.TasksCompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TasksCompletionService {
    
    @Autowired
    private TasksCompletionRepository repository;

    /**
     * 
     * @return
     */
    public TaskCompletion save(TaskCompletion taskCompletion) {
       return this.repository.save(taskCompletion); 
    }
    
    
    

}
