package giuseppetavella.demo_login_system.infrastructure.workers;

import giuseppetavella.demo_login_system.domain.entities.companies.CompaniesService;
import giuseppetavella.demo_login_system.domain.entities.contract_expectations.ContractExpectationsService;
import giuseppetavella.demo_login_system.domain.entities.notifications.NotificationsService;
import giuseppetavella.demo_login_system.domain.entities.users.UsersService;
import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.domain.entities.contract_expectations.ContractExpectation;
import giuseppetavella.demo_login_system.domain.entities.notifications.Notification;
import giuseppetavella.demo_login_system.domain.entities.users.User;
import giuseppetavella.demo_login_system.domain.entities.client_addresses.ClientAddress;
import giuseppetavella.demo_login_system.domain.entities.notifications.NotificationType;
import giuseppetavella.demo_login_system.domain.business.contract_discrepancy.ContractDiscrepancyAIDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ContractAnalysisWorker {
    
    @Autowired
    private ContractExpectationsService contractExpectationsService;
    
    @Autowired
    private ContractDiscrepancyAIDetectionService contractDiscrepancyAIDetectionService;
    
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
                                            ClientAddress clientAddress)  
    {
        
        // find the admin of this client address
        Company company = clientAddress.getClient().getCompany();
        
        User admin = this.usersService.getAdminByCompany(company);

        // potential error: no contract expectation was found
        // for this client address
        ContractExpectation contractExpectation = this.contractExpectationsService.getByClientAddress(clientAddress);
        
        try {

            // process contract with AI
            String extractedText = contractDiscrepancyAIDetectionService.extractContractExpectations(contractPdf);

            // save contract expectation as success, with extracted text 
            this.contractExpectationsService.success(
                    contractExpectation, 
                    extractedText
            );

            // note: this could throw error, but for simplicity,
            // i handle it in one try-block
            this.notifySuccess(admin, clientAddress);
            
        }
        catch(Exception ex) {
            
            // set contract expectation associated 
            // to the client address as failed
            this.contractExpectationsService.failed(contractExpectation);
            
            //  notify admin: processing failed, you can retry
            this.notifyFailure(admin, clientAddress); 
            
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
