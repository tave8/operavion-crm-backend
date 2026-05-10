package giuseppetavella.demo_login_system.payloads.in_request.reset_password;

import jakarta.validation.constraints.NotNull;

public record ResetPasswordOldPasswordSentDTO(
        
        @NotNull(message = "Missing 'oldPassword' field.")
        String oldPassword,

        @NotNull(message = "Missing 'newPassword' field.")
        String newPassword
        
) {
}
