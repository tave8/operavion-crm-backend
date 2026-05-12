package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.NotNull;

public record NewTaskSentDTO(
        
        @NotNull(message = "Missing 'name' field.")
        String name
        
) {
}
