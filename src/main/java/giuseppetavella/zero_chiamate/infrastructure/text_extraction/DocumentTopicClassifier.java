package giuseppetavella.zero_chiamate.infrastructure.text_extraction;

import giuseppetavella.zero_chiamate.exceptions.JSONDeserializationException;
import giuseppetavella.zero_chiamate.infrastructure.ai.AIService;
import giuseppetavella.zero_chiamate.infrastructure.ai.PromptBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class DocumentTopicClassifier {
    
    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @Autowired
    private PromptBuilder promptBuilder;
    
    @Autowired
    private AIService aiService;

    private final ObjectMapper mapper = new ObjectMapper();

    private final int MAX_AI_ATTEMPTS = 2;

    /**
     * Extract text or pdf documents only.
     * 
     * @param bytes
     * @param expectedTopic
     * @return
     */
    public DocumentTopicClassificationDTO classifyFromFirstLines(byte[] bytes, String expectedTopic) {
        
        int failedAttemptsCount = 0;
        
        var jsonSchema = """
        
        {
            "isExpectedTopic": boolean       // if the text's topic matches the expected topic
            "whatIfNotExpectedTopic": string|null      // null if isExpectedTopic is true, else string of whatever the actual text's topic is
        }
        
        """;
        
        // we assume the first 300 chars say this is a contract
        var startOfContract = documentTextExtractor.extractAndRequireNonEmpty(bytes, 300);

        // json payload as string
        var answerJsonToBe = aiService.ask(
                promptBuilder.classifyUserPrompt(startOfContract),
                promptBuilder.classifySystemPrompt(expectedTopic, jsonSchema)
        );
        
        // keep re-trying until AI gets it right
        while (true) {
            
            try {
                
                // deserialize json payload
                return mapper.readValue(answerJsonToBe, DocumentTopicClassificationDTO.class);
    
            } catch (JacksonException e) {
                
                failedAttemptsCount += 1;
                
                if(failedAttemptsCount > MAX_AI_ATTEMPTS) {
                    
                    throw new JSONDeserializationException(
                            "Error during deserialization of "
                            +"JSON payload from AI API "
                            +"(after attempt #" + failedAttemptsCount +"). "
                            +"Answer from AI: " + answerJsonToBe + ". DETAILS: " +e.getMessage()
                    );
                    
                }
    
    
            }
            
        }

        
    }

}
