package giuseppetavella.zero_chiamate.infrastructure.ai;

import giuseppetavella.zero_chiamate.integrations.anthropic.AnthropicAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    
    @Autowired
    private AnthropicAPIService anthropicAPIService;


    /**
     * Ask AI.
     * 
     * @return
     */
    public String ask(String prompt)
    {
        
        return anthropicAPIService.ask(prompt);
        
    }

    /**
     * Ask AI with pdf
     * 
     * @param prompt
     * @param pdf
     * @return
     */
    public String askWithPdf(String prompt, byte[] pdf)
    {
        
        return anthropicAPIService.askWithPdf(prompt, pdf);
        
    }

    
}
 