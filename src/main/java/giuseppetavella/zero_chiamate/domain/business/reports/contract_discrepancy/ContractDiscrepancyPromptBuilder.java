package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import org.springframework.stereotype.Component;

@Component
public class ContractDiscrepancyPromptBuilder {

    /**
     * Build the user prompt for contract classification.
     * 
     * @param startOfContract
     * @return
     */
    public String userPromptForContractClassification(String startOfContract) {
        return "Classify the following document opening:\n\n" + startOfContract;
    }

    
    /**
     * Build the system prompt for contract classification.
     * @return
     */
    public String systemPromptForContractClassification() {
        return  """
            You are a legal document classifier.
            You receive the opening lines of a document and determine whether it is a legal contract.
        
            Respond ONLY with a valid JSON object — no markdown, no explanation, no preamble.
        
            Schema:
            {
              "isContract": boolean,
              "whatIfNotContract": string | null
            }
            """;
    }

}
