package giuseppetavella.demo_login_system.domain.entities.tasks_completion;

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
