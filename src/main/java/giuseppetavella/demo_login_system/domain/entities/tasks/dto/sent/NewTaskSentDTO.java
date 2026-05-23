package giuseppetavella.demo_login_system.domain.entities.tasks.dto.sent;

import jakarta.validation.constraints.NotNull;

public record NewTaskSentDTO(
        
        @NotNull(message = "Missing 'name' field.")
        String name
        
) {
}
