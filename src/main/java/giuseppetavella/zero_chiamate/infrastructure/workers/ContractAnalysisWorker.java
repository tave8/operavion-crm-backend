package giuseppetavella.zero_chiamate.infrastructure.workers;

import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.TrustThisIsContract;
import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.ContractExpectationsService;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationsService;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.ContractExpectation;
import giuseppetavella.zero_chiamate.domain.entities.notifications.Notification;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddress;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationType;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ContractAnalysisWorker {
    
    @Autowired
    private ContractExpectationsService contractExpectationsService;
    
    @Autowired
    private ContractDiscrepancyDetector contractDiscrepancyDetector;
    
    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private CompaniesService companiesService;
    

    /**
     * Extract contract expectations from a contract
     * associated to a client address.
     * 
     * 
     * @param contractPdf the contract pdf in bytes
     * @param clientAddress the client address that this contract is associated to
     */
    @Async
    public void extractContractExpectations(byte[] contractPdf,
                                            ClientAddress clientAddress,
                                            TrustThisIsContract trustThisIsContract)  
    {
        
        // find the admin of this client address
        var company = clientAddress.getClient().getCompany();
        
        var admin = usersService.getAdminByCompany(company);

        // potential error: no contract expectation was found
        // for this client address
        var contractExpectation = contractExpectationsService.getByClientAddress(clientAddress);
        
        try {

            // process contract with AI
            var extractedText = contractDiscrepancyDetector.extractContractExpectations(contractPdf, trustThisIsContract);

            // save contract expectation as success, with extracted text 
            contractExpectationsService.success(
                    contractExpectation, 
                    extractedText
            );

            // note: this could throw error, but for simplicity,
            // i handle it in one try-block
            notifySuccess(admin, clientAddress);
            
        }
        catch(Exception ex) {
            
            // set contract expectation associated 
            // to the client address as failed
            contractExpectationsService.failed(contractExpectation);
            
            //  notify admin: processing failed, you can retry
            notifyFailure(admin, clientAddress); 
            
        }
        
        
    }


    /**
     * Notify admin of contract processing failure.
     */
    private void notifyFailure(User admin, 
                               ClientAddress clientAddress)
    {

        Notification newNotification = new Notification(
                admin,
                NotificationType.CONTRACT_PROCESSING_FAILED,
                "Errore processamento contratto",
                "C'è stato un errore durante il processamento "
                        +"del contratto di " + clientAddress.getClientAndAddressName() + ". Riprova."
        );

        // add a notification so admin sees processing is done    
        this.notificationsService.save(newNotification);
    }


    /**
     * Notify admin of contracting processing success.
     */
    private void notifySuccess(User admin,
                               ClientAddress clientAddress)
    {
        
        Notification newNotification = new Notification(
                admin,
                NotificationType.CONTRACT_PROCESSING_SUCCESS,
                "Contratto processato",
                "Il contratto di " + clientAddress.getClientAndAddressName() + " è stato processato. Puoi revisionarlo."
        );

        // add a notification so admin sees processing is done    
        this.notificationsService.save(newNotification);
    }


}
