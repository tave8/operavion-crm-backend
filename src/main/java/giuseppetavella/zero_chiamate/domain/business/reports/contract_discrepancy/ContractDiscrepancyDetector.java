package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import giuseppetavella.zero_chiamate.exceptions.ContractExpectationException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.exceptions.JSONDeserializationException;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTopicClassificationDTO;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTopicClassifier;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.DocumentEmptyTextExtractionException;
import giuseppetavella.zero_chiamate.infrastructure.ai.AIService;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class ContractDiscrepancyDetector {

    @Autowired
    private AIService aiService;

    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @Autowired
    private ContractDiscrepancyPromptBuilder promptBuilder;
    
    @Autowired
    private DocumentTopicClassifier documentTopicClassifier;
    
    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * Require:
     * - this document has the correct/allowed file types for text or pdf
     * - the content of this documents indicates this is an actual contract
     * 
     * @param docBytes
     */
    public void requireActualContract(byte[] docBytes) {

        DocumentTopicClassificationDTO classification;
        
        try {
            
            classification = classify(docBytes);
            
        } catch (DocumentEmptyTextExtractionException ex) {

            throw new InvalidDataException(
                    "Document uploaded is valid in file type but likely has no text. "
                    +"DETAILS: " + ex.getMessage()
            );

        }

        // document is not a contract
        if(!classification.isExpectedTopic()) {
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
            if(!classification.isExpectedTopic()) {
                throw new ContractExpectationException(
                        "This is not a contract (AI detection). "
                            +"The document is about '"+classification.whatIfNotExpectedTopic()+"' instead."
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
    public DocumentTopicClassificationDTO classify(byte[] bytes)
    {
        
        var expectedTopic = "a legal contract";
        
        return documentTopicClassifier.classifyFromFirstLines(
                bytes,
                expectedTopic
        );
        
        
    }
    

}
 