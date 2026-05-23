package giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.forgot_password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ForgotPasswordRequestWithEmailSentDTO(
        
        @NotNull(message = "Missing 'email' field.")
        @Email(message = "Email must be valid.")
        String email
        
) {
}
