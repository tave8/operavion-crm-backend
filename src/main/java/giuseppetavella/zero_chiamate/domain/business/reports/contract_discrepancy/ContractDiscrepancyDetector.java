package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.dto.ContractClassificationDTO;
import giuseppetavella.zero_chiamate.exceptions.ContractExpectationException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.exceptions.JSONDeserializationException;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.DocumentEmptyTextExtractionException;
import giuseppetavella.zero_chiamate.infrastructure.ai.AIService;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
     * Require:
     * - this document has the correct/allowed file types for text or pdf
     * - the content of this documents indicates this is an actual contract
     * 
     * @param docBytes
     */
    public void requireActualContract(byte[] docBytes) {
        
        ContractClassificationDTO classification;
        
        try {
            
            classification = classify(docBytes);
            
        } catch (DocumentEmptyTextExtractionException ex) {

            throw new InvalidDataException(
                    "Document uploaded is valid in file type but likely has no text. "
                    +"DETAILS: " + ex.getMessage()
            );

        }

        // verify that the user has uploaded an actual contract
        if(!classification.isContract()) {
            throw new InvalidDataException(
                    "Document uploaded is not a contract in its content."
            );
        }
        
    }
    
    
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
        var contractText = documentTextExtractor.extractAndRequireNonEmpty(bytes);
        
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
     * 
     * @throws JSONDeserializationException
     */
    public ContractClassificationDTO classify(byte[] bytes)
    {
        
        // we assume the first 300 chars say this is a contract
        var startOfContract = documentTextExtractor.extractAndRequireNonEmpty(bytes, 300);
        
        // json payload as string
        var answerJsonToBe = aiService.ask(
                promptBuilder.contractClassificationUserPrompt(startOfContract), 
                promptBuilder.contractClassificationSystemPrompt()
        );
        
        try {
            
            // deserialize json payload
            return mapper.readValue(answerJsonToBe, ContractClassificationDTO.class);
            
        } catch (JacksonException e) {
            
            throw new JSONDeserializationException(
                    "Error during deserialization of "
                    +"JSON payload from AI API into class. "
                    +"Answer from AI: " + answerJsonToBe + ". DETAILS: " +e.getMessage()
            );
            
        }
        
        
    }
    

}
 