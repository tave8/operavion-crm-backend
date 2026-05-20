package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.ContractExpectation;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ContractExpectationToSendDTO {
    
    private final UUID id;
    private final UUID clientAddressId;
    private final String extractedText;
    private final OffsetDateTime processedAt;
    
    public ContractExpectationToSendDTO(ContractExpectation contractExpectation) {
        this.id = contractExpectation.getId();
        this.clientAddressId = contractExpectation.getClientAddress().getId();
        this.extractedText = contractExpectation.getExtractedText();
        this.processedAt = contractExpectation.getProcessedAt();
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
