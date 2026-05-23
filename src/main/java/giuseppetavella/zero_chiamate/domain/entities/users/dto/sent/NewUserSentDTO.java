package giuseppetavella.zero_chiamate.domain.entities.users.dto.sent;

import jakarta.validation.constraints.NotNull;

public record NewUserSentDTO(

        @NotNull(message = "Missing 'firstname' field.")
        String firstname,

        @NotNull(message = "Missing 'lastname' field.")
        String lastname,

        @NotNull(message = "Missing 'role' field.")
        String role,
        
        // optional
        String email
        
) {
}
