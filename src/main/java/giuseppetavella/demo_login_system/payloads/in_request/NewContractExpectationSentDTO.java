package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.NotNull;

public record NewContractExpectationSentDTO(
        
        @NotNull(message = "Missing 'extractedText' field.")
        String extractedText
        
) {
}
