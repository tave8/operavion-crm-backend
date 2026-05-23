package giuseppetavella.zero_chiamate.domain.entities.tasks_completion.dto.sent;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewTaskCompletionSentDTO(

        @NotNull(message = "Missing 'shiftOperatorId' field.")
        UUID shiftOperatorId,

        @NotNull(message = "Missing 'checklistEntryId' field.")
        UUID checklistEntryId
        
) {
}
