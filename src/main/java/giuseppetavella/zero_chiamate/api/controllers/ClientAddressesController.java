package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.entities.checklists.ChecklistsService;
import giuseppetavella.zero_chiamate.domain.entities.checklists.dto.to_send.ChecklistToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.client_address_checklists.ClientAddressChecklistsService;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddressesService;
import giuseppetavella.zero_chiamate.domain.entities.client_address_checklists.dto.to_send.ClientAddressChecklistToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.to_send.ClientAddressToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.ContractExpectationsService;
import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.dto.ContractExpectationDTO;
import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.dto.to_send.ContractExpectationToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.ContractExpectation;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddress;
import giuseppetavella.zero_chiamate.domain.entities.client_address_checklists.ClientAddressChecklist;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.zero_chiamate.helpers.*;
import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.dto.sent.UpdatedContractExpectationSentDTO;
import giuseppetavella.zero_chiamate.infrastructure.workers.dto.to_send.BackgroundJobAcceptedDTO;
import giuseppetavella.zero_chiamate.infrastructure.workers.ContractAnalysisWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    private ContractExpectationsService contractExpectationsService;
    
    @Autowired
    private ContractAnalysisWorker contractAnalysisWorker;


    /**
     * Find the client address by ID.
     */
    @GetMapping("/{clientAddressId}")
    public ClientAddressToSendDTO getById(@AuthenticationPrincipal User currentUser,
                                          @PathVariable UUID clientAddressId)
    {

        // find client address     
        ClientAddress clientAddress = this.clientAddressesService.findById(clientAddressId);

        Company company = currentUser.getCompany();

        AuthorizationHelper.requireSameCompany(company, clientAddress.getClient().getCompany());

        ContractExpectationToSendDTO contractExpectationToSendDTO = this.contractExpectationsService.findByClientAddressDTO(clientAddress);
        
        return new ClientAddressToSendDTO(
                clientAddress,
                contractExpectationToSendDTO
        );
        
    }
    
    

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
        ValidationHelper.requireValidRange(startDate, endDate);
        
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
    public BackgroundJobAcceptedDTO extractContractExpectations(@AuthenticationPrincipal User currentUser,
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
        
        // create pending entry for this job
        // also checks that client address does not have already
        // a contract expectation
        this.contractExpectationsService.addContractExpectationIfNotExists(clientAddress);
        
        // get bytes from contract pdf
        byte[] contractPdf = FileHelper.getBytes(contractFile);

        // async worker
        this.contractAnalysisWorker.extractContractExpectations(
                contractPdf, 
                clientAddress
        );

        return new BackgroundJobAcceptedDTO(
                "Background job was accepted and is being processed."
        );
        
    }


    /**
     * Update contract expectation of this client address.
     *
     */
    @PatchMapping("/{clientAddressId}/contract-expectations")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ContractExpectationToSendDTO extractContractExpectations(@AuthenticationPrincipal User currentUser,
                                                                    @PathVariable UUID clientAddressId,
                                                                    @RequestBody @Validated UpdatedContractExpectationSentDTO body,
                                                                    BindingResult validation)
    {
        
        PayloadValidationHelper.requireNoErrors(validation);

        // find client address     
        ClientAddress clientAddress = this.clientAddressesService.findById(clientAddressId);

        Company company = currentUser.getCompany();

        // require that the client address sent must belong the the current user
        AuthorizationHelper.requireSameCompany(company, clientAddress.getClient().getCompany());

        // contract expectation must exist
        ContractExpectation contractExpectationFromDB = this.contractExpectationsService.getByClientAddress(clientAddress);
        
        // update the extracted text of this contract expectation 
        contractExpectationFromDB.setExpectations(body.expectations());
        
        this.contractExpectationsService.save(contractExpectationFromDB);

        return new ContractExpectationToSendDTO(
                new ContractExpectationDTO(contractExpectationFromDB)
        );

    }

    
    /**
     * Find contract expectations for this client address,
     * if there's one.
     * 
     * @return
     */
    @GetMapping("/{clientAddressId}/contract-expectations")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ContractExpectationToSendDTO findContractExpectations(@AuthenticationPrincipal User currentUser,
                                                                 @PathVariable UUID clientAddressId)
    {

        // find client address     
        ClientAddress clientAddress = this.clientAddressesService.findById(clientAddressId);

        Company company = currentUser.getCompany();

        // require that the client address sent must belong the the current user
        AuthorizationHelper.requireSameCompany(company, clientAddress.getClient().getCompany());
        
        Optional<ContractExpectation> maybeContractExpectation = this.contractExpectationsService.findByClientAddress(clientAddress);
        
        // contract expectation does not exist
        if(maybeContractExpectation.isEmpty()) {
            return new ContractExpectationToSendDTO();
        }
    
        // contract expectation exists
        ContractExpectation contractExpectation = maybeContractExpectation.get();
        
        return new ContractExpectationToSendDTO(
                new ContractExpectationDTO(contractExpectation)
        );
        
    }

}
