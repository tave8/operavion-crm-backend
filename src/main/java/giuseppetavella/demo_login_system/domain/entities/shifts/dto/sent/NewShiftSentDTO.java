package giuseppetavella.demo_login_system.domain.entities.shifts.dto.sent;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record NewShiftSentDTO(

        @NotNull(message = "Missing 'clientAddressId' field")
        UUID clientAddressId,

        @NotNull(message = "Missing 'checklistId' field")
        UUID checklistId,
        
        @NotNull(message = "Missing 'operatorIds' field")
        List<UUID> operatorIds,

        @NotNull(message = "Missing 'startDate' field")
        LocalDate startDate,

        // nullable — no end date means indefinite
        LocalDate endDate,  

        @NotNull(message = "Missing 'startTime' field")
        LocalTime startTime,

        @NotNull(message = "Missing 'endTime' field")
        LocalTime endTime,

        @NotNull(message = "Missing 'days' field")
        @Size(min = 1, message = "'days' must have at least one day")
        List<DayOfWeek> days
        
) {
}
