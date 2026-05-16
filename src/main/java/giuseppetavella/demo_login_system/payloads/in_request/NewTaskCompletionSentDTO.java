package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewTaskCompletionSentDTO(

        @NotNull(message = "Missing 'shiftOperatorId' field.")
        UUID shiftOperatorId,

        @NotNull(message = "Missing 'checklistEntryId' field.")
        UUID checklistEntryId
        
) {
}
