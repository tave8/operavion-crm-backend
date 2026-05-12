package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Task;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewClientSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.NewTaskSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.TaskToSendDTO;
import giuseppetavella.demo_login_system.services.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    
    @Autowired
    private TasksService tasksService;

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
