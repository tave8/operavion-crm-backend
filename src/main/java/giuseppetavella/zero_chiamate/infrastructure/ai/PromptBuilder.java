package giuseppetavella.zero_chiamate.infrastructure.ai;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {
    
    public String classifyUserPrompt(String text) {
        return "Text to classify:\n\n" + text;
    }

    public String classifySystemPrompt(String expectedTopic, String jsonSchema) {
        return """
        You are a laser-focused data extractor.
        
        You read the opening lines of a topic and determine whether it is about "%s" or not (this is the expected topic).
        
        Then you match this result against a json schema.
        
        The json schema will have to be a valid string that will be deserialized as is.
    
        Thus, respond ONLY with a valid JSON string — no explanation, no preamble, no backticks.
        
        JSON schema:
        
        %s
        """.formatted(expectedTopic, jsonSchema);
    }
    

}
