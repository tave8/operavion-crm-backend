package giuseppetavella.zero_chiamate.domain.entities.tasks.dto.sent;

import jakarta.validation.constraints.NotNull;

public record NewTaskSentDTO(
        
        @NotNull(message = "Missing 'name' field.")
        String name
        
) {
}
