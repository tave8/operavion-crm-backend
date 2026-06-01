package giuseppetavella.zero_chiamate.infrastructure.text_extraction;

import giuseppetavella.zero_chiamate.exceptions.JSONDeserializationException;
import giuseppetavella.zero_chiamate.infrastructure.ai.AIService;
import giuseppetavella.zero_chiamate.infrastructure.ai.PromptBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class DocumentClassifier {
    
    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @Autowired
    private PromptBuilder promptBuilder;
    
    @Autowired
    private AIService aiService;

    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * Extract text or pdf documents only.
     * 
     * @param bytes
     * @param expectedTopic
     * @return
     */
    public DocumentClassificationDTO classifyFromFirstLines(byte[] bytes, String expectedTopic) {
        
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

        try {

            // deserialize json payload
            return mapper.readValue(answerJsonToBe, DocumentClassificationDTO.class);

        } catch (JacksonException e) {

            throw new JSONDeserializationException(
                    "Error during deserialization of "
                            +"JSON payload from AI API into class. "
                            +"Answer from AI: " + answerJsonToBe + ". DETAILS: " +e.getMessage()
            );

        }
        
    }

}
