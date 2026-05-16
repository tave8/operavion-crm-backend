package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewTaskCompletionSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.TaskCompletionToSendDTO;
import giuseppetavella.demo_login_system.services.ChecklistEntriesService;
import giuseppetavella.demo_login_system.services.ShiftsService;
import giuseppetavella.demo_login_system.services.TasksCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks-completion")
public class TasksCompletionController {

    @Autowired
    private TasksCompletionService tasksCompletionService;
    
    @Autowired
    private ChecklistEntriesService checklistEntriesService;
    
    @Autowired
    private ShiftsService shiftsService;

    /**
     * Mark a task (checklist entry associated to the 
     * checklist of a shift of an operator) as completed.
     * 
     * Only operators can complete their own tasks
     */
    // @PostMapping
    // @PreAuthorize("hasAnyAuthority('OPERATOR')")
    // public TaskCompletionToSendDTO markAsCompleted(@AuthenticationPrincipal User currentUser,
    //                                                @RequestBody @Validated NewTaskCompletionSentDTO body,
    //                                                BindingResult validation)
    // {
    //
    //     AuthorizationHelper.requireUserOperator(currentUser);
    //
    //     PayloadValidationHelper.requireNoErrors(validation);
    //    
    //     // require that the task the user wants to set as complete
    //     // is their own task
    //    
    //     this.shiftsService.findShiftOperatorById(body.shiftOperatorId());
    //    
    //    
    // }

}
