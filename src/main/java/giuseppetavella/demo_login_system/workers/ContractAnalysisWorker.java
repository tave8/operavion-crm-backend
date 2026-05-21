package giuseppetavella.demo_login_system.workers;

import giuseppetavella.demo_login_system.entities.ContractExpectation;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.enums.internal.ContractExpectationState;
import giuseppetavella.demo_login_system.services.AppAIService;
import giuseppetavella.demo_login_system.services.ContractExpectationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ContractAnalysisWorker {
    
    @Autowired
    private ContractExpectationsService contractExpectationsService;
    
    @Autowired
    private AppAIService appAIService;
    

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
            
        }

        
        
    }

}
