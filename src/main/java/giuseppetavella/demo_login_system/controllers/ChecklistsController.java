package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewChecklistWithSimpleEntriesSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.NewTaskSentDTO;
import giuseppetavella.demo_login_system.services.ChecklistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checklists")
public class ChecklistsController {
    
    @Autowired
    private ChecklistsService checklistsService;

    /**
     * Add a checklist with "simple entries".
     * A simple entry is a checklist entry that is not yet aware
     * of the checklist it belongs to. Why? Because 
     * the client has just sent this checklist to save,
     * so we do not at this moment the checklist id.
     * A "proper" checklist entry also has a checklist id.
     * That is why we call it "simple" checklist entry.
     * 
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void addChecklistWithSimpleEntries(@RequestBody @Validated NewChecklistWithSimpleEntriesSentDTO body,
                                              BindingResult validation,
                                              @AuthenticationPrincipal User currentUser) 
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        Company company = currentUser.getCompany();
        
        this.checklistsService.addChecklistWithSimpleEntries(
                body,
                company
        );
        
    }
    
    
}
