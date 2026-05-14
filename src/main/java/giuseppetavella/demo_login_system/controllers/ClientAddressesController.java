package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.clients.ClientAddressChecklist;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_response.ChecklistToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ClientAddressChecklistToSendDTO;
import giuseppetavella.demo_login_system.services.ChecklistsService;
import giuseppetavella.demo_login_system.services.ClientAddressChecklistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client-addresses")
public class ClientAddressesController {
    
    @Autowired
    private ClientAddressChecklistsService clientAddressChecklistsService;
    
    @Autowired
    private ChecklistsService checklistsService;


    /**
     * Add a checklist to this client address.
     */
    @PostMapping("/{clientAddressId}/checklists/{checklistId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ClientAddressChecklistToSendDTO addChecklistToClientAddress(@AuthenticationPrincipal User currentUser,
                                                                       @PathVariable("clientAddressId") String clientAddressIdAsStr,
                                                                       @PathVariable("checklistId") String checklistIdAsStr)
    {

        UUID clientAddressId = StringHelper.parseUUID(clientAddressIdAsStr);
        UUID checklistId = StringHelper.parseUUID(checklistIdAsStr);

        Company company = currentUser.getCompany();

        ClientAddressChecklist clientAddressChecklistFromDB = this.clientAddressChecklistsService
                .addChecklistToClientAddress(checklistId, clientAddressId, company);

        return new ClientAddressChecklistToSendDTO(clientAddressChecklistFromDB);
        
    }

    /**
     * Get checklists by client address.
     */
    @GetMapping("/{clientAddressId}/checklists")
    public List<ChecklistToSendDTO> findChecklistsByClientAddress(@AuthenticationPrincipal User currentUser,
                                                                  @PathVariable UUID clientAddressId)
    {
        Company company = currentUser.getCompany();
        
        return this.checklistsService.findChecklistsByClientAddressDTO(
                company,
                clientAddressId
        );
    }
    

}
