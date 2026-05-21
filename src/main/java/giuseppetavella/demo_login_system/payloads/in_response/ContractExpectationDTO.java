package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.ContractExpectation;
import giuseppetavella.demo_login_system.enums.internal.ContractExpectationState;

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
    private final String extractedText;
    private final OffsetDateTime processedAt;
    
    public ContractExpectationDTO(ContractExpectation contractExpectation) {
        this.id = contractExpectation.getId();
        this.clientAddressId = contractExpectation.getClientAddress().getId();
        this.state = contractExpectation.getState();
        this.extractedText = contractExpectation.getExtractedText();
        this.processedAt = contractExpectation.getProcessedAt();
    }

    public ContractExpectationState getState() {
        return state;
    }

    public UUID getClientAddressId() {
        return clientAddressId;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }
}
