package giuseppetavella.zero_chiamate.domain.entities.contract_expectations.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.dto.ContractExpectationDTO;
import giuseppetavella.zero_chiamate.infrastructure.ContractExpectationState;


/**
 * Wrapper payload for contract expectation.
 * 
 * This is the payload we send, because 
 * it also contains other info useful for the client,
 * such as whether the contract expectation exists or not,
 * and booleans that immediately indicate the state.
 */
public class ContractExpectationToSendDTO {
    
    
    private final boolean exists;
    private final boolean pending;
    private final boolean success;
    private final boolean failed;
    private final ContractExpectationDTO detail;
    
    public ContractExpectationToSendDTO(ContractExpectationDTO contractExpectation) 
    {
        this.detail = contractExpectation;
        this.exists = contractExpectation != null;
        // we set these fields based on whether the contract expectation exists
        // and its state
        this.pending = contractExpectation != null && contractExpectation.getState().equals(ContractExpectationState.PENDING);
        this.success = contractExpectation != null && contractExpectation.getState().equals(ContractExpectationState.SUCCESS);
        this.failed = contractExpectation != null && contractExpectation.getState().equals(ContractExpectationState.FAILED);
        
    }

    /**
     * It's possible that the contract expectation does not exist,
     * so we simply tell the client "you did not make a mistake, it simply does not exist."
     */
    public ContractExpectationToSendDTO()
    {
        this(null);
    }

    public ContractExpectationDTO getDetail() {
        return detail;
    }

    public boolean isFailed() {
        return failed;
    }

    public boolean isPending() {
        return pending;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isExists() {
        return exists;
    }
}
