package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OperatorLoginSentDTO(
        
        @NotNull(message = "Missing 'username' field.")
        String username,

        @NotNull(message = "Missing 'password' field.")
        // @Size(min = 6, max = 20, message = "Password must have between 6 and 20 characters.")
        String password
) {
}
