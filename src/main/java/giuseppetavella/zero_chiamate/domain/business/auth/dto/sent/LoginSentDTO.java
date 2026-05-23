package giuseppetavella.zero_chiamate.domain.business.auth.dto.sent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginSentDTO(
        
        @NotBlank(message = "Missing 'email' field.")
        @Email(message = "Email must be valid.")
        String email,

        @NotBlank(message = "Missing 'password' field.")
        // @Size(min = 6, max = 20, message = "Password must have between 6 and 20 characters.")
        String password
) {
}
