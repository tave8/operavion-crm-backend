package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import com.fasterxml.jackson.annotation.JsonProperty;
import giuseppetavella.zero_chiamate.exceptions.ContractExpectationException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.ai.AIService;
import giuseppetavella.zero_chiamate.utils.DocumentTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class ContractDiscrepancyDetector {

    @Autowired
    private AIService aiService;

    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @Autowired
    private ContractDiscrepancyPromptBuilder promptBuilder;
    
    private final ObjectMapper mapper = new ObjectMapper();

    public record ContractClassificationDTO(
            @JsonProperty("isContract") boolean isContract,
            @JsonProperty("whatIfNotContract") String whatIfNotContract
    ) {}
    
    
    /**
     * Find discrepancies between a legal contract 
     * and the actual data of the admin's account.
     */
    public String findDiscrepancies(String contractExpectations,
                                    String actualShifts)
    {

        String prompt = "You are a compliance auditing assistant. Compare the service rules defined in "
                +"a commercial CONTRACT of a cleaning company against the ACTUAL_SHIFTS executed for "
                +"ONE specific client address of that cleaning company. The output is expectation minus reality.\n" +
                "\n" +
                "### CONTRACT SUMMARY (expectation)\n" +
                contractExpectations +
                "### ACTUAL_SHIFTS (reality)\n" +
                actualShifts +
                "### WHAT TO LOOK FOR (ANALYSIS RULES)\n" +
                "Analyze the shifts against the contract and flag only these 4 specific operational breaches:\n" +
                "1. Missing days. The contract specifies a day or frequency, but no shift was recorded on that day.\n" +
                "2. Short duration. A shift was performed, but the total time worked is shorter than what the contract dictates.\n" +
                "3. Wrong timing. A shift was performed, but the hours fall outside the specific time window requested (e.g., working during the day when the contract specifies evening hours).\n" +
                "4. If everything is missing, meaning there are no shifts, say it.\n" +
                "\n" +
                "### OUTPUT FORMAT\n" +
                "Return ONLY a plain text string in Italian listing the discrepancies found. \n" +
                "- If there are multiple discrepancies, separate them with a simple bullet point or line break.\n" +
                "- Be specific about days, expected times, and actual times.\n" +
                "- If everything matches perfectly and there are no breaches, reply exactly with: \"Nessuna discrepanza riscontrata.\"\n" +
                "- CRITICAL: The output will be read directly by the company's owner, so avoid any introductory phrases or pleasantries. Just output the raw text.";


        return aiService.ask(prompt);
    }



    /**
     * Extract expectations from legal contract.
     */
    public String extractContractExpectations(byte[] bytes)
    {

        var classification = classify(bytes);

        // this is not a contract
        if(!classification.isContract()) {
            throw new ContractExpectationException("This is not a contract (AI detection).");
        }
        
        return aiService.askWithPdfPreferText(
                bytes,
                promptBuilder.extractContractExpectationsUserPrompt(),
                promptBuilder.extractContractExpectationsSystemPrompt()
        );

    }



    public String extractContractExpectations(MultipartFile contractPdf)
    {
        byte[] bytes = FileHelper.getBytes(contractPdf);

        return extractContractExpectations(bytes);
    }

    
    /**
     * Is this file a contract?
     * Uses document extraction and AI to determine.
     */
    public ContractClassificationDTO classify(byte[] bytes) throws ContractExpectationException
    {
        // we assume the first 300 chars say this is a contract
        var startOfContract = documentTextExtractor.bytesToText(bytes, 300);
        
        //  if parser could not extract text
        if(startOfContract.isEmpty()) {
            throw new ContractExpectationException(
                    "While extracting the start of the contract, "
                    +"the result was empty. Does this file contain normal text?"
            );
        }
        
        // json payload as string
        var answerJsonToBe = aiService.ask(
                promptBuilder.contractClassificationUserPrompt(startOfContract), 
                promptBuilder.contractClassificationSystemPrompt()
        );
        
        try {
            
            return mapper.readValue(answerJsonToBe, ContractClassificationDTO.class);
            
        } catch (JacksonException e) {
            
            throw new ContractExpectationException(
                    "Error during deserialization of "
                    +"JSON payload from AI API into class. DETAILS: " +e.getMessage()
            );
            
        }
        
        
    }
    

}
 