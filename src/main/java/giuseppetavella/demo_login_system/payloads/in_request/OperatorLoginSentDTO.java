package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OperatorLoginSentDTO(
        
        @NotBlank(message = "Missing 'username' field.")
        String username,

        @NotBlank(message = "Missing 'password' field.")
        // @Size(min = 6, max = 20, message = "Password must have between 6 and 20 characters.")
        String password
) {
}
