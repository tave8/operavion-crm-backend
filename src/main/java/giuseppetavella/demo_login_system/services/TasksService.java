package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Task;
import giuseppetavella.demo_login_system.payloads.in_request.NewTaskSentDTO;
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


    /**
     * Add this task payload to this company.
     * 
     * @param body
     * @param company
     * @return
     */
    public Task addTask(NewTaskSentDTO body, Company company)
    {
     
        Task task = new Task(company, body.name());
        
        return this.save(task);
    }
    
    
}
