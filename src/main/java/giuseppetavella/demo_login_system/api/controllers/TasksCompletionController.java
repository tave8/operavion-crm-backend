package giuseppetavella.demo_login_system.api.controllers;

import giuseppetavella.demo_login_system.domain.entities.checklist_entries.ChecklistEntriesService;
import giuseppetavella.demo_login_system.domain.entities.shifts.ShiftsService;
import giuseppetavella.demo_login_system.domain.entities.tasks_completion.TasksCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
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
