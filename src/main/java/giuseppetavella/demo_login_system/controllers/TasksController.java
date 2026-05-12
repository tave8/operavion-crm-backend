package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Client;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Task;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewClientSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.NewTaskSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ClientToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.TaskToSendDTO;
import giuseppetavella.demo_login_system.services.TasksService;
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
                                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
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
