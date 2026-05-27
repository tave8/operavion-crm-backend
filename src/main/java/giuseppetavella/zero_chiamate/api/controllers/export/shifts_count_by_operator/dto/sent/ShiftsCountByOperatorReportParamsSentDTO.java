package giuseppetavella.zero_chiamate.api.controllers.export.shifts_count_by_operator.dto.sent;

import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public record ShiftsCountByOperatorReportParamsSentDTO(
        @NotNull(message = "Missing 'startDate' field.")
        LocalDate startDate,

        @NotNull(message = "Missing 'endDate' field.")
        LocalDate endDate
) {
}
