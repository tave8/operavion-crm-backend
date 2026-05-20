package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.ContractExpectation;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.payloads.in_request.NewContractExpectationSentDTO;
import giuseppetavella.demo_login_system.repositories.ContractExpectationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractExpectationsService {
    
    @Autowired
    private ContractExpectationsRepository repo;

    /**
     * Save a ncontract expectation.
     */
    public ContractExpectation addContractExpectation(NewContractExpectationSentDTO body,
                                                        ClientAddress clientAddress) 
    {
        ContractExpectation contractExpectation = new ContractExpectation(
                clientAddress,
                body.extractedText()
        );
        
        return this.repo.save(contractExpectation);    
    }

    /**
     * Save contract expectation.
     * @return
     */
    public ContractExpectation save(ContractExpectation contractExpectation) {
        return this.repo.save(contractExpectation);
    }
    
    
}
