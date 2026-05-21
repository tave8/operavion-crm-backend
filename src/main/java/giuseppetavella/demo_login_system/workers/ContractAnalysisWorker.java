package giuseppetavella.demo_login_system.workers;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.ContractExpectation;
import giuseppetavella.demo_login_system.entities.Notification;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.enums.NotificationType;
import giuseppetavella.demo_login_system.enums.internal.ContractExpectationState;
import giuseppetavella.demo_login_system.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ContractAnalysisWorker {
    
    @Autowired
    private ContractExpectationsService contractExpectationsService;
    
    @Autowired
    private AppAIService appAIService;
    
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


        // ***************************
        // EXTRACT PDF 
        // ***************************
        
        try {

            System.out.println("started extracting contract");
            // extract text
            String contractExpectationsFromAI = this.appAIService.extractContractExpectations(contractPdf);

            System.out.println("finished extracting contract");

            ContractExpectation contractExpectation = this.contractExpectationsService.findByClientAddress(clientAddress);

            // save contract expectation as success, with extracted text 
            this.contractExpectationsService.success(contractExpectation, contractExpectationsFromAI); 
            
        }
        catch(Exception ex) {

            // there was an error
            // send email with error to developer

            System.out.println("error while extracting contract");

            ContractExpectation contractExpectation = this.contractExpectationsService.findByClientAddress(clientAddress);

            this.contractExpectationsService.failed(contractExpectation);
            
            // ***************************
            // NOTIFY ADMIN: PROCESSING FAILED, YOU CAN RETRY
            // ***************************
            
            // find the admin of this client address
            Company company = clientAddress.getClient().getCompany();

            User admin = this.usersService.getAdminByCompany(company);

            Notification newNotification = new Notification(
                    admin,
                    NotificationType.CONTRACT_PROCESSING_FAILED,
                    "Errore processamento contratto",
                    "C'è stato un errore durante il processamento "
                            +"del contratto di " + clientAddress.getClient().getLegalName() + ". Riprova."
            );

            // add a notification so admin sees processing is done    
            this.notificationsService.save(newNotification);

        }


        // ***************************
        // NOTIFY ADMIN: PROCESSING WAS SUCCESS, YOU CAN REVIEW 
        // ***************************
        
        try {
            
            // find the admin of this client address
            Company company = clientAddress.getClient().getCompany();
    
            User admin = this.usersService.getAdminByCompany(company);
    
            Notification newNotification = new Notification(
                    admin,
                    NotificationType.CONTRACT_PROCESSING_SUCCESS,
                    "Contratto processato",
                    "Il contratto di " + clientAddress.getClient().getLegalName() + " è stato processato. Puoi revisionarlo."
            );
    
            // add a notification so admin sees processing is done    
            this.notificationsService.save(newNotification);
    
        }
        catch (Exception ex) {

            System.out.println("error while adding notification");

            // ContractExpectation contractExpectation = this.contractExpectationsService.findByClientAddress(clientAddress);

            // this.contractExpectationsService.failed(contractExpectation);
            
        }
        
    }

}
