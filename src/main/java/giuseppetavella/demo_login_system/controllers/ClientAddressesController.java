package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.entities.clients.ClientAddressChecklist;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.*;
import giuseppetavella.demo_login_system.payloads.in_response.ChecklistToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ClientAddressChecklistToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ProfileToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ShiftToSendDTO;
import giuseppetavella.demo_login_system.services.ChecklistsService;
import giuseppetavella.demo_login_system.services.ClientAddressChecklistsService;
import giuseppetavella.demo_login_system.services.ClientAddressesService;
import giuseppetavella.demo_login_system.services.ShiftsService;
import giuseppetavella.demo_login_system.workers.ContractAnalysisWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client-addresses")
public class ClientAddressesController {
    
    @Autowired
    private ClientAddressChecklistsService clientAddressChecklistsService;
    
    @Autowired
    private ChecklistsService checklistsService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    @Autowired
    private ContractAnalysisWorker contractAnalysisWorker;



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


    /**
     * Find operators whose shifts take place at the input client address,
     * optionally filtering shifts by date range.
     */
    @GetMapping("/{clientAddressId}/shifts/operators")
    public List<ProfileToSendDTO> findOperatorsByClientAddress(@AuthenticationPrincipal User currentUser,
                                                               @PathVariable UUID clientAddressId,
                                                               @RequestParam(value = "from", required = false) LocalDate startDate,
                                                               @RequestParam(value = "to", required = false) LocalDate endDate)
    {
        DataValidationHelper.requireValidRange(startDate, endDate);
        
        // find client address     
        ClientAddress clientAddress = this.clientAddressesService.findById(clientAddressId);

        Company company = currentUser.getCompany();
        
        AuthorizationHelper.requireSameCompany(company, clientAddress.getClient().getCompany());
        
        return this.shiftsService.findOperatorsByClientAddressBetweenDatesDTO(
                clientAddress,
                startDate,
                endDate
        );
        
    }



    /**
     * Extract contract expectations from a legal contract.
     *
     * @param contractFile
     * @return
     */
    @PostMapping("/{clientAddressId}/contract-expectations")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    // status code: 202
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String extractContractExpectations(@AuthenticationPrincipal User currentUser,
                                              @PathVariable UUID clientAddressId,
                                              @RequestParam("file") MultipartFile contractFile)
    {
        
        // the contract must be a pdf
        PayloadValidationHelper.requiredPdf(contractFile);

        // find client address     
        ClientAddress clientAddress = this.clientAddressesService.findById(clientAddressId);

        Company company = currentUser.getCompany();
        
        // require that the client address sent must belong the the current user
        AuthorizationHelper.requireSameCompany(company, clientAddress.getClient().getCompany());
        

        byte[] contractPdf = FileHelper.getBytes(contractFile);

        // async worker
        this.contractAnalysisWorker.extractContractExpectations(contractPdf, clientAddress);

        return "ok";

        // String contractExpectationsFromAI = this.appAIService.extractContractExpectations(contractFile);
        //
        // return new ExtractedContractExpectationsToSendDTO(
        //         contractExpectationsFromAI
        // );

    }


}
