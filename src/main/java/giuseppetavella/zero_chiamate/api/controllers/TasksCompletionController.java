package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.ChecklistEntriesService;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.tasks_completion.TasksCompletionService;
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
