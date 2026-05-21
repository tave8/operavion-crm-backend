package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.ContractExpectation;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.enums.internal.ContractExpectationState;
import giuseppetavella.demo_login_system.exceptions.ContractExpectationException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.payloads.in_request.NewContractExpectationSentDTO;
import giuseppetavella.demo_login_system.repositories.ContractExpectationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ContractExpectationsService {
    
    @Autowired
    private ContractExpectationsRepository repo;
    
    
    /**
     * <pre>
     *     
     * Contract expectation for this client address not exists?
     *    add it
     *    
     * Contract expectation for this client address exists AND is failed? 
     *   don't add it, user can retry
     * 
     * Contract expectation for this client address exists AND is pending or success?
     *   cannot add it, throw error
     *   
     *</pre>
     */
    public void addContractExpectationIfNotExists(ClientAddress clientAddress) 
    {
        
        Optional<ContractExpectation> maybeContractExpectation =  this.repo.findByClientAddress(clientAddress);
        
        // if contract expectation does not exist, add it
        if(maybeContractExpectation.isEmpty()) {
            
            ContractExpectation contractExpectation = new ContractExpectation(clientAddress);
            
            this.repo.save(contractExpectation);
            
            return;
        }
        
        // because contract expectation exists, 
        // we must check its state
        
        ContractExpectation contractExpectationFromDB = maybeContractExpectation.get();
        
        // if existing contract expectation is success or pending,
        // we cannot add a new one
        boolean isSuccessful = contractExpectationFromDB.getState().equals(ContractExpectationState.SUCCESS);
        boolean isPending = contractExpectationFromDB.getState().equals(ContractExpectationState.PENDING);
        boolean isFailed = contractExpectationFromDB.getState().equals(ContractExpectationState.FAILED);
        
        if(isSuccessful || isPending) 
        {
            
            throw new ContractExpectationException(
                    "While adding a new contract expectation, cannot add a new contract expectation. "
                    +"Reason: For client address with ID '" + clientAddress.getId()+ "', " 
                    +"there exists a contract expectation with state "+contractExpectationFromDB.getState()+"."
            );
            
        }
        
        // if existing contract expectation has failed state, 
        // we do not add it, and we allow to retry processing.
        // so we reset state to pending
        if(isFailed) {
            
            // because the contract expectation is failed, 
            // we now retry processing it
            contractExpectationFromDB.setState(ContractExpectationState.PENDING);
            this.save(contractExpectationFromDB);
            
            return;
        }
        
        
        throw new ContractExpectationException("Code path was not defined. INTERNAL ERROR.");
        
    }


    /**
     * Save contract expectation as failed.
     * 
     * @return
     */
    public ContractExpectation failed(ContractExpectation contractExpectation)
    {
        
        contractExpectation.setState(ContractExpectationState.FAILED);
        return this.save(contractExpectation);
        
    }
    

    /**
     * Save contract expectation as success, with extracted text.
     *
     * @return
     */
    public ContractExpectation success(ContractExpectation contractExpectation,
                                        String extractedText)
    {

        contractExpectation.setState(ContractExpectationState.SUCCESS);
        contractExpectation.setExtractedText(extractedText);
        return this.save(contractExpectation);

    }

    
    /**
     * Find a contract expectation by ID.
     */
    public ContractExpectation findById(UUID contractExpectationId) throws NotFoundException {
        return repo.findById(contractExpectationId)
                   .orElseThrow(() -> new NotFoundException(contractExpectationId, "contract expectation"));
    }


    /**
     * Find the contract expectation of a client address.
     */
    public ContractExpectation findByClientAddress(ClientAddress clientAddress) throws NotFoundException {
        return repo
                .findByClientAddress(clientAddress)
                .orElseThrow(() -> new NotFoundException("Could not find a contract expectation "
                                                        +"from client address with ID '" + clientAddress.getId()+"'"));
    }


    /**
     * The contract expectation with this ID exists?
     * 
     */
    public boolean existsById(UUID contractExpectationId) {
        if(contractExpectationId == null) {
            throw new ContractExpectationException("While checking if contract expectation "
                    +"exists by ID, ID cannot be null.");

        }
        return this.repo.existsById(contractExpectationId);
    }


    /**
     * The contract expectation for this client address exists?
     *
     */
    public boolean existsByClientAddress(ClientAddress clientAddress) {
        if(clientAddress == null) {
            throw new ContractExpectationException("While checking if contract expectation "
                                                    +"exists by client address, client address cannot be null.");
        }
        return this.repo.existsByClientAddress(clientAddress);
    }
    
    
    
    /**
     * Save contract expectation.
     * @return
     */
    public ContractExpectation save(ContractExpectation contractExpectation) {
        return this.repo.save(contractExpectation);
    }
    
    
}
