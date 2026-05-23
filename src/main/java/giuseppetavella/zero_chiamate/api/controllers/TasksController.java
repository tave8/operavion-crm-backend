package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.tasks.Task;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.helpers.StringHelper;
import giuseppetavella.zero_chiamate.domain.entities.tasks.dto.sent.NewTaskSentDTO;
import giuseppetavella.zero_chiamate.domain.entities.tasks.dto.to_send.TaskToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.tasks.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    
    @Autowired
    private TasksService tasksService;


    /*
     * Get tasks.
     * */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<TaskToSendDTO> getTasks(@AuthenticationPrincipal User currentUser,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "pageSize", defaultValue = "100") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                                            @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
                                            @RequestParam(value = "name", defaultValue = "") String taskName)
    {

        // sortBy must be one of these values
        StringHelper.requireInValues(
                sortBy,
                List.of("name"),
                "sortBy"
        );

        StringHelper.requireInValues(
                sortOrder,
                List.of("asc", "desc"),
                "sortOrder"
        );

        Company company = currentUser.getCompany();

        Page<Task> tasksPage = this.tasksService.findTasksByName(
                company,
                taskName,
                page,
                pageSize,
                sortBy,
                sortOrder
        );

        return tasksPage.map(task -> new TaskToSendDTO(task));

    }
    

    /**
     * Add a task to my company.
     * 
     * @param body
     * @param validation
     * @param currentUser
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public TaskToSendDTO addTask(@RequestBody @Validated NewTaskSentDTO body,
                                 BindingResult validation,
                                 @AuthenticationPrincipal User currentUser) 
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        Company company = currentUser.getCompany();
        
        Task taskFromDB = this.tasksService.addTask(body, company);
        
        return new TaskToSendDTO(taskFromDB);
        
    }

}
