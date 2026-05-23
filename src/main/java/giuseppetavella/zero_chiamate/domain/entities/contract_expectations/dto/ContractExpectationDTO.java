package giuseppetavella.zero_chiamate.domain.entities.contract_expectations.dto;

import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.ContractExpectation;
import giuseppetavella.zero_chiamate.infrastructure.ContractExpectationState;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * This payload can be nested inside ContractExpectationToSendDTO,
 * and contains the actual contract expectation, if it exists.
 */
public class ContractExpectationDTO {
    
    private final UUID id;
    private final UUID clientAddressId;
    private final ContractExpectationState state;
    private final String expectations;
    private final OffsetDateTime processedAt;
    
    public ContractExpectationDTO(ContractExpectation contractExpectation) {
        this.id = contractExpectation.getId();
        this.clientAddressId = contractExpectation.getClientAddress().getId();
        this.state = contractExpectation.getState();
        this.expectations = contractExpectation.getExpectations();
        this.processedAt = contractExpectation.getProcessedAt();
    }

    public ContractExpectationState getState() {
        return state;
    }

    public UUID getClientAddressId() {
        return clientAddressId;
    }

    public String getExpectations() {
        return expectations;
    }

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }
}
