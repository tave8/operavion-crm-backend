package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.params;

import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.ClientAddressDiscrepancyDTO;

import java.time.LocalDate;
import java.util.List;

public record ContractDiscrepancyReportParams(
        List<ClientAddressDiscrepancyDTO> discrepancies,
        LocalDate startDate,
        LocalDate endDate
) {}