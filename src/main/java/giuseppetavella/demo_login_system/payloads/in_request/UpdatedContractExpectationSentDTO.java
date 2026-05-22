package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.NotNull;

public record UpdatedContractExpectationSentDTO(
        
        @NotNull(message = "Missing 'expectations' field.")
        String expectations
        
) {
}
