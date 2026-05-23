package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.entities.checklists.Checklist;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.helpers.StringHelper;
import giuseppetavella.zero_chiamate.domain.entities.checklists.dto.sent.NewChecklistWithSimpleEntriesSentDTO;
import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.dto.to_send.ChecklistEntryToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.checklists.dto.to_send.ChecklistToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.ChecklistEntriesService;
import giuseppetavella.zero_chiamate.domain.entities.checklists.ChecklistsService;
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
    
    @Autowired
    private ChecklistEntriesService checklistEntriesService;

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
    public ChecklistToSendDTO addChecklistWithSimpleEntries(@RequestBody @Validated NewChecklistWithSimpleEntriesSentDTO body,
                                              BindingResult validation,
                                              @AuthenticationPrincipal User currentUser) 
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        Company company = currentUser.getCompany();
        
        Checklist checklistFromDB = this.checklistsService.addChecklistWithSimpleEntries(body, company);
        
        List<ChecklistEntryToSendDTO> entries = this.checklistEntriesService.getEntriesByChecklistAsDTO(checklistFromDB);
        
        return new ChecklistToSendDTO(checklistFromDB, entries);
        
    }



    /*
     * Get checklists.
     * */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<ChecklistToSendDTO> getChecklists(@AuthenticationPrincipal User currentUser,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                 @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                                                 @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
                                                  @RequestParam(value = "q", defaultValue = "") String query)
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
                query,
                page,
                pageSize,
                sortBy,
                sortOrder
        );

        return checklistsPage.map(checklist -> {
            List<ChecklistEntryToSendDTO> entries = this.checklistEntriesService.getEntriesByChecklistAsDTO(checklist);
            return new ChecklistToSendDTO(checklist, entries);
        });

    }



}
