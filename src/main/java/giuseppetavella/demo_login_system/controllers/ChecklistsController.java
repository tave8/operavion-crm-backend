package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Checklist;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Task;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewChecklistWithSimpleEntriesSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.NewTaskSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ChecklistToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.TaskToSendDTO;
import giuseppetavella.demo_login_system.services.ChecklistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



    /*
     * Get checklists.
     * */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<ChecklistToSendDTO> getTasks(@AuthenticationPrincipal User currentUser,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                                             @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder)
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

        Page<Checklist> checklistsPage = this.checklistsService.findChecklists(
                company,
                page,
                pageSize,
                sortBy,
                sortOrder
        );

        return checklistsPage.map(checklist -> new ChecklistToSendDTO(checklist));

    }



}
