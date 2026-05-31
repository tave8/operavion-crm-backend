package giuseppetavella.zero_chiamate.infrastructure.ai;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.integrations.anthropic.AnthropicAPIService;
import giuseppetavella.zero_chiamate.utils.DocumentTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    @Autowired
    private AnthropicAPIService anthropicAPIService;

    @Autowired
    private DocumentTextExtractor documentTextExtractor;


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

    

    public String askWithImage(byte[] imageBytes, String userPrompt, String systemPrompt)
    {

        ValidationHelper.requireFileImage(imageBytes);
        
        var mediaType = FileHelper.getMimeType(imageBytes);

        return anthropicAPIService.askWithImage(imageBytes, mediaType, userPrompt, systemPrompt);

    }


    public String askWithImage(byte[] imageBytes, String userPrompt)
    {

        ValidationHelper.requireFileImage(imageBytes);

        var mediaType = FileHelper.getMimeType(imageBytes);

        return anthropicAPIService.askWithImage(imageBytes, mediaType, userPrompt);

    }



    /**
     * Run a prompt against a PDF, preferring cheap deterministic extraction.
     *
     * First tries to read the PDF's text layer with {@link DocumentTextExtractor}
     * (no AI, no vision tokens). If text is found, the prompt runs against that
     * text via {@link #ask(String, String)}. If the PDF has no text layer
     * (e.g. a scanned/photographed document), it falls back to the AI vision
     * path {@link #askWithPdf(byte[], String, String)}, which OCRs the document.
     *
     * @param pdfBytes     the PDF bytes
     * @param userPrompt   what to ask about the document's content
     * @param systemPrompt the system prompt
     * @return the AI response
     */
    public String askWithPdfPreferText(byte[] pdfBytes, String userPrompt, String systemPrompt)
    {

        ValidationHelper.requireFilePdf(pdfBytes);

        String extractedText = documentTextExtractor.bytesToText(pdfBytes);

        // no text layer (scanned/image PDF) -> fall back to AI vision/OCR
        if (extractedText.isEmpty()) {
            return anthropicAPIService.askWithPdf(pdfBytes, userPrompt, systemPrompt);
        }

        // text layer present -> answer over the extracted text (cheaper, no vision tokens)
        String combinedPrompt = userPrompt + "\n\n--- DOCUMENT TEXT ---\n" + extractedText;

        return anthropicAPIService.ask(combinedPrompt, systemPrompt);

    }


    public String askWithPdfPreferText(byte[] pdfBytes, String userPrompt)
    {

        ValidationHelper.requireFilePdf(pdfBytes);

        String extractedText = documentTextExtractor.bytesToText(pdfBytes);

        if (extractedText.isEmpty()) {
            return anthropicAPIService.askWithPdf(pdfBytes, userPrompt);
        }

        String combinedPrompt = userPrompt + "\n\n--- DOCUMENT TEXT ---\n" + extractedText;

        return anthropicAPIService.ask(combinedPrompt);

    }


}
 