package giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator.params;

import giuseppetavella.zero_chiamate.domain.entities.users.User;

import java.time.LocalDate;
import java.util.Map;

public record ShiftsCountByOperatorReportParams(
        Map<User, Integer> shiftsCountByOperator,
        
        LocalDate startDate,
        
        LocalDate endDate
) {
}
