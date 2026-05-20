package giuseppetavella.demo_login_system.workers;

import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ContractAnalysisWorker {

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
                                            ClientAddress clientAddress)  {
        
        try {
            
            Thread.sleep(5000);
            System.out.println("hello from worker thread");
            System.out.println(clientAddress);
            System.out.println(contractPdf);
            
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
    }

}
