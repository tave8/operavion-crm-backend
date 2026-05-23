package giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.reset_password;

import jakarta.validation.constraints.NotNull;

public record ResetPasswordOldPasswordSentDTO(
        
        @NotNull(message = "Missing 'oldPassword' field.")
        String oldPassword,

        @NotNull(message = "Missing 'newPassword' field.")
        String newPassword
        
) {
}
