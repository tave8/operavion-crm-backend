package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.UUID;

/**
 * This is a "simple entry" because it does not contain
 * the checklist id. It is used when the client sends
 * the entries.
 * 
 * @param taskId
 * @param position
 */
public record ChecklistSimpleEntrySentDTO(

        @NotNull(message = "Missing 'taskId' field")
        @UUID(message = "Field 'taskId' must be a valid UUID.")
        String taskId,

        @NotNull(message = "Missing 'position' field")
        @Positive(message = "Field 'position' must be >= 1")
        Integer position
        
) {
}
