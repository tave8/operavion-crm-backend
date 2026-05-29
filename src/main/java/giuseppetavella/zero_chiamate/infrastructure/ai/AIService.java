package giuseppetavella.zero_chiamate.infrastructure.ai;

import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
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
    public String ask(String userPrompt)
    {
        
        return anthropicAPIService.ask(userPrompt);
        
    }


    public String ask(String userPrompt, String systemPrompt)
    {

        return anthropicAPIService.ask(userPrompt, systemPrompt);

    }

    

    /**
     * Ask AI with pdf
     * 
     * @return
     */
    public String askWithPdf(byte[] pdfBytes, String userPrompt)
    {

        ValidationHelper.requireFilePdf(pdfBytes);
        
        return anthropicAPIService.askWithPdf(pdfBytes, userPrompt);
        
    }


    public String askWithPdf(byte[] pdfBytes, String userPrompt, String systemPrompt)
    {
        
        ValidationHelper.requireFilePdf(pdfBytes);
        
        return anthropicAPIService.askWithPdf(pdfBytes, userPrompt, systemPrompt);

    }
    
}
 