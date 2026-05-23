package giuseppetavella.demo_login_system.domain.entities.tasks;

import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.domain.entities.tasks.dto.sent.NewTaskSentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TasksService {
    
    @Autowired
    private TasksRepository tasksRepository;

    /**
     * Find a task by ID
     */
    public Task findById(UUID taskId) throws NotFoundException 
    {
        return this.tasksRepository.findById(taskId).orElseThrow(() -> new NotFoundException(taskId, "TASK"));
    }
    
    /**
     * Save a task to DB.
     * @return
     */
    public Task save(Task task) 
    {
        return this.tasksRepository.save(task);
    }
    
    public List<Task> saveAll(List<Task> tasks) 
    {
        return this.tasksRepository.saveAll(tasks);    
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


    /**
     * Find tasks by name.
     */
    public Page<Task> findTasksByName(Company company,
                                     String taskName,
                                     int page,
                                     int pageSize,
                                     String sortBy,
                                     String sortOrder) throws InvalidDataException
    {

        // we can sort by these values
        StringHelper.requireInValues(
                sortBy,
                List.of("name"),
                "sortBy"
        );

        // we can sort in these "directions"
        StringHelper.requireInValues(
                sortOrder,
                List.of("asc", "desc"),
                "sortOrder"
        );

        // number of elements in page
        int finalSize = Math.clamp(pageSize, 1, 100);

        // which pagination page was requested
        int finalPage = Math.max(0, page);

        Sort sort = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(finalPage, finalSize, sort);

        // we assume there's no task name
        String taskNamePattern = null;

        // create pattern for task name, if a task name was specified
        // if task name is not blank or empty
        if(!taskName.trim().isEmpty()) {
            // create partial match pattern with task name in lower case
            taskNamePattern = "%" + taskName.toLowerCase().trim() + "%";
        }

        return this.tasksRepository.findTasksByName(
                company,
                taskNamePattern,
                pageable
        );

    }
    
    
}
