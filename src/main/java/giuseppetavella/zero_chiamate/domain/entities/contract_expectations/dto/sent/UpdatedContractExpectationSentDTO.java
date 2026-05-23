package giuseppetavella.zero_chiamate.domain.entities.contract_expectations.dto.sent;

import jakarta.validation.constraints.NotNull;

public record UpdatedContractExpectationSentDTO(
        
        @NotNull(message = "Missing 'expectations' field.")
        String expectations
        
) {
}
