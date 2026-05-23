package giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.forgot_password;

import jakarta.validation.constraints.NotNull;

public record VerifyForgotPasswordCodeSentDTO(
        
        @NotNull(message = "Missing 'code' field.")
        String code
        
) {
}
