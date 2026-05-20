package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.ContractExpectation;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.exceptions.ContractExpectationException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.payloads.in_request.NewContractExpectationSentDTO;
import giuseppetavella.demo_login_system.repositories.ContractExpectationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContractExpectationsService {
    
    @Autowired
    private ContractExpectationsRepository repo;
    
    
    /**
     * Check that a contract expectations does not exist first,
     * and if not, add it.
     * 
     * Save a new contract expectation.
     * Because we assume the extraction occurs in the background,
     * we don't have the extracted text yet, only the client address
     * to which this extract belongs.
     */
    public ContractExpectation addContractExpectation(ClientAddress clientAddress) 
    {
        // contract expectation must be unique per client address
        if(this.existsByClientAddress(clientAddress)) {
            throw new ContractExpectationException("Duplicate contract expectation. While adding a new contract expectation "
                                                    +"for client address with ID '" + clientAddress.getId()+ "', "
                                                    +"this client address already has a contract expectation.");
        }
        
        ContractExpectation contractExpectation = new ContractExpectation(clientAddress);
        
        return this.repo.save(contractExpectation);    
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
