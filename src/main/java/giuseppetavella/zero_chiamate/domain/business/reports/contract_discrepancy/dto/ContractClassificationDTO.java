package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * 
 * @param isContract
 * @param whatIfNotContract
 */
public record ContractClassificationDTO(
            // is this a contract?
            @JsonProperty("isContract") boolean isContract,
            
            // if it's not a contract, what is it?
            // useful for debugging or correcting
            @JsonProperty("whatIfNotContract") String whatIfNotContract
    ) {}