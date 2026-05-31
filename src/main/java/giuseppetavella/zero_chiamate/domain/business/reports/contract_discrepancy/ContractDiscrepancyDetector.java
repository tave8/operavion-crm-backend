package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import com.fasterxml.jackson.annotation.JsonProperty;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.dto.ContractClassificationDTO;
import giuseppetavella.zero_chiamate.exceptions.ContractExpectationException;
import giuseppetavella.zero_chiamate.exceptions.DocumentTextExtractionException;
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
    
    
    
    /**
     * Find discrepancies between a legal contract 
     * and the actual data of the admin's account.
     */
    public String findDiscrepancies(String contractExpectations,
                                    String actualShifts)
    {

        return aiService.ask(
                promptBuilder.findDiscrepanciesUserPrompt(contractExpectations, actualShifts),
                promptBuilder.findDiscrepanciesSystemPrompt()
        );
        
    }



    /**
     * Extract expectations from legal contract.
     * Choose whether to make an extra check if this is a contract,
     * by setting <code>trust</code> to NO.
     * 
     */
    public String extractContractExpectations(byte[] bytes, 
                                              TrustThisIsContract trust)
    {
    
        // if we don't trust this is a contract, 
        // we check if it's a contract
        if(trust.no()) {
            var classification = classify(bytes);

            // this is not a contract
            if(!classification.isContract()) {
                throw new ContractExpectationException(
                        "This is not a contract (AI detection). "
                            +"The document is about '"+classification.whatIfNotContract()+"' instead."
                );
            }
        }
        
        // extract the text from contract
        var contractText = documentTextExtractor.bytesToText(bytes);
        
        return aiService.ask(
                promptBuilder.extractContractExpectationsUserPrompt(contractText),
                promptBuilder.extractContractExpectationsSystemPrompt()
        );

    }

    
    /**
     * By default, we don't trust this is a contract.
     * 
     * @param bytes
     * @return
     */
    public String extractContractExpectations(byte[] bytes) 
    {
        return extractContractExpectations(bytes, TrustThisIsContract.NO);
    }
    
    

    
    /**
     * Is this file a contract?
     * Uses document extraction and AI to determine.
     */
    public ContractClassificationDTO classify(byte[] bytes) throws DocumentTextExtractionException, 
                                                                    ContractExpectationException
    {
        
        // we assume the first 300 chars say this is a contract
        var startOfContract = documentTextExtractor.bytesToText(bytes, 300);
        
        // json payload as string
        var answerJsonToBe = aiService.ask(
                promptBuilder.contractClassificationUserPrompt(startOfContract), 
                promptBuilder.contractClassificationSystemPrompt()
        );
        
        try {
            
            // deserialize json payload
            return mapper.readValue(answerJsonToBe, ContractClassificationDTO.class);
            
        } catch (JacksonException e) {
            
            throw new ContractExpectationException(
                    "Error during deserialization of "
                    +"JSON payload from AI API into class. DETAILS: " +e.getMessage()
            );
            
        }
        
        
    }
    

}
 